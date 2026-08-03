package com.sga.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarUsuarioDTO(
    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres")
    String username,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String password,

    @NotBlank(message = "O nome completo é obrigatório")
    String nome,

    @NotBlank(message = "A role (função) é obrigatória")
    String role // "ROLE_ATENDENTE" ou "ROLE_ADMIN"
) {}
