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

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Nginx proxy cuidará das origens em prod
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
        return senhaService.chamarProximaSenha(dto.guiche(), dto.tipoFila())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nenhuma senha aguardando na fila."));
    }

    // Guichê - Rechamar Senha
    @PostMapping("/atendimento/{id}/rechamar")
    public ResponseEntity<Senha> rechamarSenha(@PathVariable Long id) {
        return senhaService.rechamarSenha(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Guichê - Concluir Atendimento
    @PostMapping("/atendimento/{id}/concluir")
    public ResponseEntity<Senha> concluirAtendimento(@PathVariable Long id) {
        return senhaService.concluirAtendimento(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
