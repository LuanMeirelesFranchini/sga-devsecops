package com.sga.dto;

public record EstatisticaRelatorioDTO(
        long totalAtendimentos,
        String tempoMedioEspera,
        String tempoMedioAtendimento
) {}
