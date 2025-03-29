package com.api.service;

import com.api.dto.SolicitacaoRequest;
import com.api.dto.SolicitacaoResponse;
import com.api.model.Solicitacao;
import com.api.repository.SolicitacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SolicitacaoServiceTest {

    @Mock
    private SolicitacaoRepository repository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private SolicitacaoService solicitacaoService;

    private SolicitacaoRequest solicitacaoRequest;

    private Solicitacao solicitacao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        solicitacaoRequest = new SolicitacaoRequest();
        solicitacaoRequest.setCep("12345678");

        solicitacao = new Solicitacao();
        solicitacao.setCep("12345678");
        solicitacao.setProtocolo("ABC123");
    }

    @Test
    public void testProcessarNovaSolicitacao() {
        when(repository.save(Mockito.any(Solicitacao.class))).thenReturn(solicitacao);

        doNothing().when(rabbitTemplate).convertAndSend(Mockito.anyString(), Mockito.anyString(), Mockito.any(SolicitacaoRequest.class));

        SolicitacaoResponse response = solicitacaoService.processarNovaSolicitacao(solicitacaoRequest);

        assertNotNull(response);
        assertEquals("Solicitação recebida e em processamento", response.getMensagem(), "O mensagem gerada deve ser 'Solicitação recebida e em processamento'");

        verify(rabbitTemplate, times(1)).convertAndSend("solicitacao.exchange", "solicitacao.routingkey", solicitacaoRequest);

        verify(repository, times(1)).save(Mockito.any(Solicitacao.class));
    }

    @Test
    public void testBuscarPorProtocolo_Success() {
        when(repository.findByProtocolo("ABC123")).thenReturn(java.util.Optional.of(solicitacao));

        Solicitacao result = solicitacaoService.buscarPorProtocolo("ABC123");

        assertNotNull(result);
        assertEquals("ABC123", result.getProtocolo(), "O protocolo deve ser 'ABC123'");
    }

    @Test
    public void testBuscarPorProtocolo_NotFound() {
        when(repository.findByProtocolo("XYZ456")).thenReturn(java.util.Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            solicitacaoService.buscarPorProtocolo("XYZ456");
        });
    }
}
