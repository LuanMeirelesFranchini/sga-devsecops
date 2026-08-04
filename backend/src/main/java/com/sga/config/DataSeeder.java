package com.sga.config;

import com.sga.model.Usuario;
import com.sga.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${sga.security.admin.username}")
    private String adminUsername;

    @Value("${sga.security.admin.password}")
    private String adminPassword;

    public DataSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        
        // Em produção, NUNCA criamos usuários "dummy" como atendente1 e store1.
        // Todos os usuários devem ser criados pelo Administrador via Painel.

        // Cria o Administrador Padrão a partir da Variável de Ambiente (NÃO HARDCODED)
        if (!usuarioRepository.existsByUsername(adminUsername)) {
            
            // Medida extra de segurança: Se a senha for a padrão da properties (não configurada no .env)
            // geramos uma senha forte aleatória e imprimimos no console do servidor.
            String finalPassword = adminPassword;
            if (finalPassword == null || finalPassword.equals("CHANGE_ME_IMMEDIATELY") || finalPassword.isBlank()) {
                finalPassword = UUID.randomUUID().toString().substring(0, 12);
                System.out.println("==================================================================");
                System.out.println("⚠️ ALERTA DE SEGURANÇA (DEVSECOPS) ⚠️");
                System.out.println("Nenhuma senha de admin foi definida na variável ADMIN_PASSWORD.");
                System.out.println("Uma senha temporária aleatória foi gerada para o admin: " + finalPassword);
                System.out.println("Anote esta senha, faça login e altere no Painel Admin imediatamente.");
                System.out.println("==================================================================");
            }

            usuarioRepository.save(new Usuario(
                adminUsername,
                passwordEncoder.encode(finalPassword),
                "Administrador de Sistema (Seguro)",
                "ROLE_ADMIN"
            ));
        }
    }
}
