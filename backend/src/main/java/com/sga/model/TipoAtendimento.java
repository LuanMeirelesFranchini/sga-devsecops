package com.sga.model;

public enum TipoAtendimento {
    PREFERENCIAL("PRE", "Atendimento Preferencial (Idosos, Gestantes, PCD)"),
    NORMAL("NOR", "Atendimento Convencional / Normal"),
    EXAMES("EXA", "Entrega e Coleta de Exames");

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
