package com.sga.controller;

import com.sga.dto.AuthResponseDTO;
import com.sga.dto.LoginDTO;
import com.sga.model.Usuario;
import com.sga.repository.UsuarioRepository;
import com.sga.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider tokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        Optional<Usuario> userOpt = usuarioRepository.findByUsername(loginDTO.username());

        if (userOpt.isPresent() && passwordEncoder.matches(loginDTO.password(), userOpt.get().getPassword())) {
            Usuario user = userOpt.get();
            String token = tokenProvider.generateToken(user.getUsername(), user.getRole(), user.getNome());
            return ResponseEntity.ok(new AuthResponseDTO(token, user.getUsername(), user.getNome(), user.getRole()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos.");
    }
}
