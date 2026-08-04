package com.sga.service;

import com.sga.dto.EstatisticaFilaDTO;
import com.sga.model.Senha;
import com.sga.model.SetorAtendimento;
import com.sga.model.StatusSenha;
import com.sga.model.TipoAtendimento;
import com.sga.repository.SenhaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SenhaService {

    private final SenhaRepository senhaRepository;
    private final SseService sseService;

    public SenhaService(SenhaRepository senhaRepository, SseService sseService) {
        this.senhaRepository = senhaRepository;
        this.sseService = sseService;
    }

    @Transactional
    public Senha gerarSenha(TipoAtendimento tipo) {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        Integer ultimoNumero = senhaRepository.findMaxNumeroByTipoEData(tipo, inicioDia).orElse(0);
        Integer novoNumero = ultimoNumero + 1;

        String codigoFormatado = String.format("%s-%03d", tipo.getSigla(), novoNumero);

        Senha novaSenha = new Senha(codigoFormatado, tipo, novoNumero);
        Senha senhaSalva = senhaRepository.save(novaSenha);
        
        // Dispara evento SSE para o frontend (passando o objeto JSON da senha)
        sseService.dispatch("nova_senha", senhaSalva);
        
        return senhaSalva;
    }

    @Transactional
    public Optional<Senha> chamarProximaSenha(String guiche, SetorAtendimento setor, TipoAtendimento tipoFila) {
        List<Senha> aguardando;

        if (tipoFila != null) {
            aguardando = senhaRepository.findByStatusOrderByDataCriacaoAsc(StatusSenha.AGUARDANDO)
                    .stream().filter(s -> s.getTipo() == tipoFila).toList();
        } else if (setor == SetorAtendimento.STORE) {
            aguardando = senhaRepository.findProximaSenhaStore(StatusSenha.AGUARDANDO);
        } else if (setor == SetorAtendimento.RECEPCAO) {
            aguardando = senhaRepository.findProximaSenhaRecepcao(StatusSenha.AGUARDANDO);
        } else if (setor == SetorAtendimento.DIRECAO) {
            aguardando = senhaRepository.findByStatusOrderByDataCriacaoAsc(StatusSenha.AGUARDANDO)
                    .stream().filter(s -> s.getTipo() == TipoAtendimento.DIRECAO_AGENDADO).toList();
        } else {
            aguardando = senhaRepository.findProximaSenhaGeral(StatusSenha.AGUARDANDO);
        }

        if (aguardando.isEmpty()) {
            return Optional.empty();
        }

        Senha proxima = aguardando.get(0);
        proxima.setStatus(StatusSenha.CHAMADO);
        proxima.setGuiche(guiche);
        proxima.setDataChamada(LocalDateTime.now());

        return Optional.of(senhaRepository.save(proxima));
    }

    @Transactional
    public Optional<Senha> rechamarSenha(Long idSenha) {
        return senhaRepository.findById(idSenha).map(senha -> {
            senha.setDataChamada(LocalDateTime.now());
            return senhaRepository.save(senha);
        });
    }

    @Transactional
    public Optional<Senha> concluirAtendimento(Long idSenha, String observacao) {
        return senhaRepository.findById(idSenha).map(senha -> {
            senha.setStatus(StatusSenha.ATENDIDO);
            senha.setObservacao(observacao);
            senha.setDataAtendimento(LocalDateTime.now());
            return senhaRepository.save(senha);
        });
    }

    public List<Senha> obterUltimasChamadasPainel() {
        return senhaRepository.findTop10ByStatusOrderByDataChamadaDesc(StatusSenha.CHAMADO);
    }

    public EstatisticaFilaDTO obterEstatisticasFila() {
        long total = senhaRepository.countByStatus(StatusSenha.AGUARDANDO);
        long recPref = senhaRepository.countByTipoAndStatus(TipoAtendimento.RECEPCAO_PREFERENCIAL, StatusSenha.AGUARDANDO);
        long recNorm = senhaRepository.countByTipoAndStatus(TipoAtendimento.RECEPCAO_NORMAL, StatusSenha.AGUARDANDO);
        long stoPref = senhaRepository.countByTipoAndStatus(TipoAtendimento.STORE_PREFERENCIAL, StatusSenha.AGUARDANDO);
        long stoNorm = senhaRepository.countByTipoAndStatus(TipoAtendimento.STORE_NORMAL, StatusSenha.AGUARDANDO);

        return new EstatisticaFilaDTO(total, recPref, recNorm, stoPref, stoNorm);
    }
}
