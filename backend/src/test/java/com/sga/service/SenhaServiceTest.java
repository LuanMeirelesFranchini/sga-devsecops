package com.sga.service;

import com.sga.dto.EstatisticaFilaDTO;
import com.sga.model.Senha;
import com.sga.model.StatusSenha;
import com.sga.model.TipoAtendimento;
import com.sga.repository.SenhaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SenhaServiceTest {

    @Mock
    private SenhaRepository senhaRepository;

    @Mock
    private SseService sseService;

    @InjectMocks
    private SenhaService senhaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGerarSenhaRecepcaoNormal() {
        // Arrange
        when(senhaRepository.findMaxNumeroByTipoEData(any(), any())).thenReturn(Optional.of(5));
        
        Senha senhaMockada = new Senha("REC-006", TipoAtendimento.RECEPCAO_NORMAL, 6);
        when(senhaRepository.save(any(Senha.class))).thenReturn(senhaMockada);

        // Act
        Senha gerada = senhaService.gerarSenha(TipoAtendimento.RECEPCAO_NORMAL);

        // Assert
        assertNotNull(gerada);
        assertEquals("REC-006", gerada.getCodigo());
        assertEquals(TipoAtendimento.RECEPCAO_NORMAL, gerada.getTipo());
        assertEquals(StatusSenha.AGUARDANDO, gerada.getStatus());
    }

    @Test
    void testEstatisticasFilaVazia() {
        // Arrange
        when(senhaRepository.countByStatus(StatusSenha.AGUARDANDO)).thenReturn(0L);
        when(senhaRepository.countByTipoAndStatus(any(), any())).thenReturn(0L);

        // Act
        EstatisticaFilaDTO stats = senhaService.obterEstatisticasFila();

        // Assert
        assertEquals(0, stats.totalAguardando());
        assertEquals(0, stats.recepcaoNormal());
        assertEquals(0, stats.storePreferencial());
    }
}
