package com.sga.controller;

import com.sga.dto.ChamarSenhaDTO;
import com.sga.dto.ConcluirAtendimentoDTO;
import com.sga.dto.EstatisticaFilaDTO;
import com.sga.dto.GerarSenhaDTO;
import com.sga.model.Senha;
import com.sga.model.SetorAtendimento;
import com.sga.model.StatusSenha;
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

    // Guichê - Chamar Próxima Senha (Filtrado por Setor do Atendente)
    @PostMapping("/atendimento/chamar")
    public ResponseEntity<?> chamarProxima(@Valid @RequestBody ChamarSenhaDTO dto) {
        try {
            Optional<Senha> senhaOpt = senhaService.chamarProximaSenha(dto.guiche(), dto.setor(), dto.tipoFila());
            if (senhaOpt.isPresent()) {
                return ResponseEntity.ok(senhaOpt.get());
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // Guichê - Obter Senha Atual do Guichê
    @GetMapping("/atendimento/atual")
    public ResponseEntity<Senha> obterSenhaAtual(@RequestParam String guiche) {
        Optional<Senha> senhaOpt = senhaService.obterSenhaAtual(guiche);
        if (senhaOpt.isPresent()) {
            return ResponseEntity.ok(senhaOpt.get());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Guichê - Listar Filas (Aguardando / Pausados)
    @GetMapping("/atendimento/fila")
    public ResponseEntity<List<Senha>> listarFila(@RequestParam SetorAtendimento setor, @RequestParam StatusSenha status) {
        return ResponseEntity.ok(senhaService.listarFila(setor, status));
    }

    // Guichê - Rechamar Senha
    @PostMapping("/atendimento/{id}/rechamar")
    public ResponseEntity<Senha> rechamarSenha(@PathVariable Long id) {
        Optional<Senha> senhaOpt = senhaService.rechamarSenha(id);
        if (senhaOpt.isPresent()) {
            return ResponseEntity.ok(senhaOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Guichê - Concluir Atendimento com Comentário Opcional
    @PostMapping("/atendimento/{id}/concluir")
    public ResponseEntity<Senha> concluirAtendimento(
            @PathVariable Long id,
            @RequestBody(required = false) ConcluirAtendimentoDTO dto) {

        String observacao = (dto != null) ? dto.observacao() : null;
        Optional<Senha> senhaOpt = senhaService.concluirAtendimento(id, observacao);

        if (senhaOpt.isPresent()) {
            return ResponseEntity.ok(senhaOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Guichê - Pausar Atendimento
    @PostMapping("/atendimento/{id}/pausar")
    public ResponseEntity<Senha> pausarSenha(@PathVariable Long id) {
        Optional<Senha> senhaOpt = senhaService.pausarSenha(id);
        if (senhaOpt.isPresent()) {
            return ResponseEntity.ok(senhaOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Guichê - Retomar Atendimento Pausado
    @PostMapping("/atendimento/{id}/retomar")
    public ResponseEntity<Senha> retomarSenha(@PathVariable Long id) {
        Optional<Senha> senhaOpt = senhaService.retomarSenha(id);
        if (senhaOpt.isPresent()) {
            return ResponseEntity.ok(senhaOpt.get());
        }
        return ResponseEntity.notFound().build();
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
