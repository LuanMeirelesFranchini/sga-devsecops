package com.sga.dto;

public record EstatisticaFilaDTO(
    long totalAguardando,
    long recepcaoPreferencial,
    long recepcaoNormal,
    long storePreferencial,
    long storeNormal
) {}
