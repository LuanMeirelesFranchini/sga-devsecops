package com.sga.dto;

public record UsuarioDTO(
    Long id,
    String username,
    String nome,
    String role
) {}
