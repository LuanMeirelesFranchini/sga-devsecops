package com.sga.controller;

import com.sga.dto.EstatisticaRelatorioDTO;
import com.sga.model.Senha;
import com.sga.service.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/diario")
    public ResponseEntity<EstatisticaRelatorioDTO> obterEstatisticaDiaria() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioDiario());
    }

    @GetMapping("/historico")
    public ResponseEntity<List<Senha>> obterHistorico() {
        return ResponseEntity.ok(relatorioService.obterHistoricoAtendimentos());
    }
}
