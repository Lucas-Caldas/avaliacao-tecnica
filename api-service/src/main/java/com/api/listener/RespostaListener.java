package com.api.listener;

import com.api.dto.RespostaMessage;
import com.api.repository.SolicitacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
public class RespostaListener {

    private final SolicitacaoRepository repository;

    @Autowired
    public RespostaListener(SolicitacaoRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "resposta.queue")
    @Transactional
    public void processarResposta(RespostaMessage resposta) {
        try {
            if (resposta == null || resposta.getProtocolo() == null) {
                log.error("Mensagem recebida sem protocolo válido! Ignorando...");
                return;
            }

            log.info("Mensagem recebida na fila. Protocolo: {}", resposta.getProtocolo());

            repository.findByProtocolo(resposta.getProtocolo())
                    .ifPresentOrElse(
                            solicitacao -> {
                                solicitacao.setDadosApiExterna(resposta.getDadosProcessados());
                                solicitacao.setStatus(resposta.getStatus());
                                solicitacao.setDataAtualizacao(LocalDateTime.now());

                                if ("ERRO".equals(resposta.getStatus())) {
                                    solicitacao.setDadosApiExterna(resposta.getMensagemErro());
                                }

                                repository.save(solicitacao);
                                log.info("Protocolo {} atualizado com sucesso!", resposta.getProtocolo());
                            },
                            () -> log.warn("Nenhuma solicitação encontrada para o protocolo: {}", resposta.getProtocolo())
                    );

        } catch (Exception e) {
            log.error("Erro ao processar a mensagem da fila: {}", e.getMessage(), e);
        }
    }
}
