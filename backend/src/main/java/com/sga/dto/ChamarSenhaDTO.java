package com.sga.dto;

import com.sga.model.TipoAtendimento;
import jakarta.validation.constraints.NotBlank;

public record ChamarSenhaDTO(
    @NotBlank(message = "O nome ou número do guichê é obrigatório")
    String guiche,

    TipoAtendimento tipoFila // Opcional: Se null, chama a mais antiga respeitando prioridade
) {}
