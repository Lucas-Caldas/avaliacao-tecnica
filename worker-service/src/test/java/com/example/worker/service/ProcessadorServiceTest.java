package com.example.worker.service;

import com.example.worker.dto.RespostaMessage;
import com.example.worker.dto.SolicitacaoMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProcessadorServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ProcessadorService processadorService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessarSolicitacao_Sucesso() {
        // Criando mensagem de solicitação
        SolicitacaoMessage mensagem = new SolicitacaoMessage();
        mensagem.setProtocolo("ac59e62f-2438-41d2-90b3-f099e8ec3f18");
        mensagem.setCep("12345678");

        processadorService.processarSolicitacao(mensagem);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ArgumentCaptor<RespostaMessage> captor = ArgumentCaptor.forClass(RespostaMessage.class);
        verify(rabbitTemplate, timeout(1000)).convertAndSend(eq("solicitacao.exchange"), eq("resposta.routingkey"), captor.capture());

        RespostaMessage respostaEnviada = captor.getValue();
        assertNotNull(respostaEnviada);
        assertEquals("ac59e62f-2438-41d2-90b3-f099e8ec3f18", respostaEnviada.getProtocolo());
    }

    @Test
    public void testProcessarSolicitacao_Erro() {
        SolicitacaoMessage mensagem = new SolicitacaoMessage();
        mensagem.setProtocolo("ac59e62f-2438-41d2-90b3-f099e8ec3f18");
        mensagem.setCep("00000000");

        processadorService.processarSolicitacao(mensagem);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ArgumentCaptor<RespostaMessage> captor = ArgumentCaptor.forClass(RespostaMessage.class);
        verify(rabbitTemplate, timeout(1000)).convertAndSend(eq("solicitacao.exchange"), eq("resposta.routingkey"), captor.capture());

        RespostaMessage respostaEnviada = captor.getValue();
        assertNotNull(respostaEnviada);
        assertEquals("ac59e62f-2438-41d2-90b3-f099e8ec3f18", respostaEnviada.getProtocolo());
        assertEquals("ERRO", respostaEnviada.getStatus());
    }
}
