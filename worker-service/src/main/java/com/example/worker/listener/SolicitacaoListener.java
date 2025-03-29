package com.example.worker.listener;

import com.example.worker.dto.SolicitacaoMessage;
import com.example.worker.service.ProcessadorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoListener {

    @Autowired
    private ProcessadorService processadorService;

    @RabbitListener(queues = "solicitacao.queue")
    public void receberSolicitacao(SolicitacaoMessage mensagem) {
        processadorService.processarSolicitacao(mensagem);
    }

}