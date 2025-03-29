package com.example.worker.listener;

import com.example.worker.dto.SolicitacaoMessage;
import com.example.worker.service.ProcessadorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class SolicitacaoListenerTest {

    @Mock
    private ProcessadorService processadorService;

    @InjectMocks
    private SolicitacaoListener solicitacaoListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReceberSolicitacao_DeveChamarProcessadorService() {
        SolicitacaoMessage mensagem = new SolicitacaoMessage();
        mensagem.setProtocolo("ABC123");
        mensagem.setCep("12345678");

        solicitacaoListener.receberSolicitacao(mensagem);

        verify(processadorService, times(1)).processarSolicitacao(mensagem);
    }
}
