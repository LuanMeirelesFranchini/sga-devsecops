package com.sga.dto;

import com.sga.model.TipoAtendimento;
import jakarta.validation.constraints.NotNull;

public record GerarSenhaDTO(
    @NotNull(message = "O tipo de atendimento é obrigatório")
    TipoAtendimento tipo
) {}
