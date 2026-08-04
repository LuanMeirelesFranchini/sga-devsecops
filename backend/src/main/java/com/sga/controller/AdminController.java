package com.sga.controller;

import com.sga.dto.CriarUsuarioDTO;
import com.sga.dto.UsuarioDTO;
import com.sga.model.Usuario;
import com.sga.repository.SenhaRepository;
import com.sga.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final SenhaRepository senhaRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UsuarioRepository usuarioRepository,
                           SenhaRepository senhaRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.senhaRepository = senhaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Listar todos os usuários
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioRepository.findAll().stream()
                .map(u -> new UsuarioDTO(u.getId(), u.getUsername(), u.getNome(), u.getRole()))
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    // Criar novo usuário (Atendente ou Admin)
    @PostMapping("/usuarios")
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody CriarUsuarioDTO dto) {
        if (usuarioRepository.existsByUsername(dto.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe um usuário cadastrado com esse nome de usuário.");
        }

        Usuario novoUsuario = new Usuario(
                dto.username(),
                passwordEncoder.encode(dto.password()),
                dto.nome(),
                dto.role()
        );

        Usuario salvo = usuarioRepository.save(novoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UsuarioDTO(salvo.getId(), salvo.getUsername(), salvo.getNome(), salvo.getRole()));
    }

    // Redefinir Senha de um usuário
    @PostMapping("/usuarios/{id}/alterar-senha")
    public ResponseEntity<?> alterarSenha(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String novaSenha = payload.get("novaSenha");
        if (novaSenha == null || novaSenha.trim().length() < 6) {
            return ResponseEntity.badRequest().body("A nova senha deve ter no mínimo 6 caracteres.");
        }

        return usuarioRepository.findById(id).map(user -> {
            user.setPassword(passwordEncoder.encode(novaSenha));
            usuarioRepository.save(user);
            return ResponseEntity.ok("Senha alterada com sucesso!");
        }).orElse(ResponseEntity.notFound().build());
    }

    // Excluir usuário por ID
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Resetar Filas (Zerar chamadas e senhas)
    @PostMapping("/reset-filas")
    public ResponseEntity<?> resetarFilas() {
        senhaRepository.deleteAll();
        return ResponseEntity.ok("Filas de atendimento resetadas com sucesso!");
    }
}
