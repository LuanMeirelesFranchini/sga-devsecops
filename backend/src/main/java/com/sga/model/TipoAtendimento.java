package com.sga.model;

public enum TipoAtendimento {
    RECEPCAO_PREFERENCIAL("PRE", "Recepção - Atendimento Prioritário (Idosos, Gestantes, PCD)"),
    RECEPCAO_NORMAL("REC", "Recepção Geral / Secretaria / Matrículas"),
    STORE_UNIFORMES("STO", "La Salle Store - Uniformes e Artigos");

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
