package com.sga.service;

import com.sga.dto.EstatisticaRelatorioDTO;
import com.sga.model.Senha;
import com.sga.model.StatusSenha;
import com.sga.repository.SenhaRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class RelatorioService {

    private final SenhaRepository senhaRepository;

    public RelatorioService(SenhaRepository senhaRepository) {
        this.senhaRepository = senhaRepository;
    }

    public EstatisticaRelatorioDTO gerarRelatorioDiario() {
        List<Senha> atendidos = senhaRepository.findByStatusOrderByDataCriacaoAsc(StatusSenha.ATENDIDO);
        
        long totalAtendimentos = atendidos.size();
        
        if (totalAtendimentos == 0) {
            return new EstatisticaRelatorioDTO(0, "00:00:00", "00:00:00");
        }

        long totalEsperaSegundos = 0;
        long totalAtendimentoSegundos = 0;

        for (Senha s : atendidos) {
            if (s.getDataCriacao() != null && s.getDataChamada() != null) {
                totalEsperaSegundos += Math.abs(Duration.between(s.getDataCriacao(), s.getDataChamada()).getSeconds());
            }
            if (s.getDataChamada() != null && s.getDataAtendimento() != null) {
                totalAtendimentoSegundos += Math.abs(Duration.between(s.getDataChamada(), s.getDataAtendimento()).getSeconds());
            }
        }

        long mediaEsperaSegundos = totalEsperaSegundos / totalAtendimentos;
        long mediaAtendimentoSegundos = totalAtendimentoSegundos / totalAtendimentos;

        return new EstatisticaRelatorioDTO(
                totalAtendimentos,
                formatarSegundos(mediaEsperaSegundos),
                formatarSegundos(mediaAtendimentoSegundos)
        );
    }
    
    public List<Senha> obterHistoricoAtendimentos() {
        return senhaRepository.findTop10ByStatusOrderByDataChamadaDesc(StatusSenha.ATENDIDO);
    }

    private String formatarSegundos(long segundos) {
        long s = segundos % 60;
        long m = (segundos / 60) % 60;
        long h = (segundos / (60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
