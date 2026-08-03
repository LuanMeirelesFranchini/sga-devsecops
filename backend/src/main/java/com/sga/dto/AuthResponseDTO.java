package com.sga.dto;

public record AuthResponseDTO(
    String token,
    String username,
    String nome,
    String role
) {}
