package com.api.repository;

import com.api.model.Solicitacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class SolicitacaoRepositoryTest {

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @InjectMocks
    private SolicitacaoRepositoryTest solicitacaoRepositoryTest;

    private Solicitacao solicitacao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        solicitacao = new Solicitacao();
        solicitacao.setProtocolo("12345");
    }

    @Test
    public void testFindByProtocolo_Success() {
        when(solicitacaoRepository.findByProtocolo("12345")).thenReturn(Optional.of(solicitacao));

        // Executando o método
        Optional<Solicitacao> result = solicitacaoRepository.findByProtocolo("12345");

        assertTrue(result.isPresent(), "Deve retornar um valor presente");
        assertEquals("12345", result.get().getProtocolo(), "O protocolo deve ser igual a 12345");
    }

    @Test
    public void testFindByProtocolo_NotFound() {
        when(solicitacaoRepository.findByProtocolo("67890")).thenReturn(Optional.empty());
        Optional<Solicitacao> result = solicitacaoRepository.findByProtocolo("67890");
        assertFalse(result.isPresent(), "Deve retornar um valor vazio");
    }
}
