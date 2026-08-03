package com.sga.service;

import com.sga.dto.EstatisticaFilaDTO;
import com.sga.model.Senha;
import com.sga.model.StatusSenha;
import com.sga.model.TipoAtendimento;
import com.sga.repository.SenhaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class SenhaService {

    private final SenhaRepository senhaRepository;

    public SenhaService(SenhaRepository senhaRepository) {
        this.senhaRepository = senhaRepository;
    }

    @Transactional
    public Senha gerarSenha(TipoAtendimento tipo) {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        Integer ultimoNumero = senhaRepository.findMaxNumeroByTipoEData(tipo, inicioDia).orElse(0);
        Integer novoNumero = ultimoNumero + 1;

        String codigoFormatado = String.format("%s-%03d", tipo.getSigla(), novoNumero);

        Senha novaSenha = new Senha(codigoFormatado, tipo, novoNumero);
        return senhaRepository.save(novaSenha);
    }

    @Transactional
    public Optional<Senha> chamarProximaSenha(String guiche, TipoAtendimento tipoFila) {
        List<Senha> aguardando;
        if (tipoFila != null) {
            aguardando = senhaRepository.findByStatusOrderByDataCriacaoAsc(StatusSenha.AGUARDANDO)
                    .stream().filter(s -> s.getTipo() == tipoFila).toList();
        } else {
            aguardando = senhaRepository.findProximaSenhaFilaPrioritaria();
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
    public Optional<Senha> concluirAtendimento(Long idSenha) {
        return senhaRepository.findById(idSenha).map(senha -> {
            senha.setStatus(StatusSenha.ATENDIDO);
            senha.setDataAtendimento(LocalDateTime.now());
            return senhaRepository.save(senha);
        });
    }

    public List<Senha> obterUltimasChamadasPainel() {
        return senhaRepository.findTop10ByStatusOrderByDataChamadaDesc(StatusSenha.CHAMADO);
    }

    public EstatisticaFilaDTO obterEstatisticasFila() {
        long total = senhaRepository.countByStatus(StatusSenha.AGUARDANDO);
        long pref = senhaRepository.countByTipoAndStatus(TipoAtendimento.PREFERENCIAL, StatusSenha.AGUARDANDO);
        long norm = senhaRepository.countByTipoAndStatus(TipoAtendimento.NORMAL, StatusSenha.AGUARDANDO);
        long exam = senhaRepository.countByTipoAndStatus(TipoAtendimento.EXAMES, StatusSenha.AGUARDANDO);

        return new EstatisticaFilaDTO(total, pref, norm, exam);
    }
}
