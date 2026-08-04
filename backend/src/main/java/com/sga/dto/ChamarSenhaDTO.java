package com.sga.dto;

import com.sga.model.SetorAtendimento;
import com.sga.model.TipoAtendimento;
import jakarta.validation.constraints.NotBlank;

public record ChamarSenhaDTO(
    @NotBlank(message = "O nome ou número do guichê é obrigatório")
    String guiche,

    SetorAtendimento setor, // RECEPCAO, STORE, TODOS

    TipoAtendimento tipoFila // Opcional
) {}
