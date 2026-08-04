package com.sga.model;

public enum TipoAtendimento {
    RECEPCAO_PREFERENCIAL("REC-P", "Recepção - Atendimento Prioritário"),
    RECEPCAO_NORMAL("REC", "Recepção Geral / Secretaria / Matrículas"),
    STORE_PREFERENCIAL("STO-P", "La Salle Store - Atendimento Prioritário"),
    STORE_NORMAL("STO", "La Salle Store - Uniformes e Materiais"),
    DIRECAO_AGENDADO("AGE", "Direção - Atendimento Agendado");

    private final String sigla;
    private final String descricao;

    TipoAtendimento(String sigla, String descricao) {
        this.sigla = sigla;
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public String getDescricao() {
        return descricao;
    }
}
