package com.api.listener;

import com.api.dto.RespostaMessage;
import com.api.model.Solicitacao;
import com.api.repository.SolicitacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RespostaListenerTest {

    @InjectMocks
    private RespostaListener respostaListener;

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    private Solicitacao solicitacaoMock;
    private RespostaMessage respostaMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        solicitacaoMock = new Solicitacao();
        solicitacaoMock.setProtocolo("12345");
        solicitacaoMock.setStatus("PROCESSANDO");
        solicitacaoMock.setDadosApiExterna("Dados Processados");
        solicitacaoMock.setDataAtualizacao(LocalDateTime.now());
        respostaMessage = new RespostaMessage("12345", "Dados Processados");
    }

    @Test
    void testProcessarResposta_ComMensagemValida_DeveAtualizarSolicitacao() {
        when(solicitacaoRepository.findByProtocolo("12345")).thenReturn(Optional.of(solicitacaoMock));

        respostaListener.processarResposta(respostaMessage);

        assertEquals("PROCESSADO", solicitacaoMock.getStatus());
        assertEquals("Dados Processados", solicitacaoMock.getDadosApiExterna());

        verify(solicitacaoRepository, times(1)).save(solicitacaoMock);
    }

    @Test
    void testProcessarResposta_ComMensagemComErro_DeveAtualizarSolicitacaoComErro() {
        // Simula uma resposta com status de erro
        respostaMessage.setStatus("ERRO");
        respostaMessage.setMensagemErro("Erro ao processar");

        // Mock para encontrar a solicitação no banco de dados
        when(solicitacaoRepository.findByProtocolo("12345")).thenReturn(Optional.of(solicitacaoMock));

        // Chama o método a ser testado
        respostaListener.processarResposta(respostaMessage);

        // Verifica se a solicitação foi atualizada com a mensagem de erro
        assertEquals("Erro ao processar", solicitacaoMock.getDadosApiExterna());

        // Verifica se o método save foi chamado
        verify(solicitacaoRepository, times(1)).save(solicitacaoMock);
    }

    @Test
    void testProcessarResposta_SemProtocolo_DeveIgnorar() {
        RespostaMessage respostaInvalida = new RespostaMessage(null,  "Dados Processados", "400 Bad Request from GET https://viacep.com.br/ws/json");

        respostaListener.processarResposta(respostaInvalida);

        verify(solicitacaoRepository, never()).save(any(Solicitacao.class));
    }

    @Test
    void testProcessarResposta_ComProtocoloNaoEncontrado_DeveIgnorar() {

        when(solicitacaoRepository.findByProtocolo("12345")).thenReturn(Optional.empty());

        respostaListener.processarResposta(respostaMessage);

        verify(solicitacaoRepository, never()).save(any(Solicitacao.class));
    }

}
