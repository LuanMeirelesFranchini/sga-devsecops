package com.sga.config;

import com.sga.model.Usuario;
import com.sga.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByUsername("atendente1")) {
            usuarioRepository.save(new Usuario(
                "atendente1",
                passwordEncoder.encode("senha123"),
                "Atendente Recepção 01",
                "ROLE_ATENDENTE"
            ));
        }

        if (!usuarioRepository.existsByUsername("store1")) {
            usuarioRepository.save(new Usuario(
                "store1",
                passwordEncoder.encode("senha123"),
                "Atendente La Salle Store",
                "ROLE_ATENDENTE"
            ));
        }

        if (!usuarioRepository.existsByUsername("admin")) {
            usuarioRepository.save(new Usuario(
                "admin",
                passwordEncoder.encode("admin123"),
                "Administrador Geral",
                "ROLE_ADMIN"
            ));
        }
    }
}
