package com.sga.controller;

import com.sga.dto.ChamarSenhaDTO;
import com.sga.dto.EstatisticaFilaDTO;
import com.sga.dto.GerarSenhaDTO;
import com.sga.model.Senha;
import com.sga.service.SenhaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SenhaController {

    private final SenhaService senhaService;

    public SenhaController(SenhaService senhaService) {
        this.senhaService = senhaService;
    }

    // Totem Kiosk - Gerar Senha (Público)
    @PostMapping("/senhas/gerar")
    public ResponseEntity<Senha> gerarSenha(@Valid @RequestBody GerarSenhaDTO dto) {
        Senha novaSenha = senhaService.gerarSenha(dto.tipo());
        return ResponseEntity.status(HttpStatus.CREATED).body(novaSenha);
    }

    // Guichê - Chamar Próxima Senha
    @PostMapping("/atendimento/chamar")
    public ResponseEntity<?> chamarProxima(@Valid @RequestBody ChamarSenhaDTO dto) {
        Optional<Senha> senhaOpt = senhaService.chamarProximaSenha(dto.guiche(), dto.tipoFila());
        if (senhaOpt.isPresent()) {
            return ResponseEntity.ok(senhaOpt.get());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Guichê - Rechamar Senha
    @PostMapping("/atendimento/{id}/rechamar")
    public ResponseEntity<Senha> rechamarSenha(@PathVariable Long id) {
        Optional<Senha> senhaOpt = senhaService.rechamarSenha(id);
        return senhaOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Guichê - Concluir Atendimento
    @PostMapping("/atendimento/{id}/concluir")
    public ResponseEntity<Senha> concluirAtendimento(@PathVariable Long id) {
        Optional<Senha> senhaOpt = senhaService.concluirAtendimento(id);
        return senhaOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Painel TV - Obter chamadas recentes
    @GetMapping("/senhas/painel")
    public ResponseEntity<List<Senha>> obterPainel() {
        return ResponseEntity.ok(senhaService.obterUltimasChamadasPainel());
    }

    // Guichê/Dashboard - Obter estatísticas das filas
    @GetMapping("/senhas/estatisticas")
    public ResponseEntity<EstatisticaFilaDTO> obterEstatisticas() {
        return ResponseEntity.ok(senhaService.obterEstatisticasFila());
    }
}
