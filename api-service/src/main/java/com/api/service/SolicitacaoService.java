package com.api.service;

import com.api.dto.SolicitacaoRequest;
import com.api.dto.SolicitacaoResponse;
import com.api.repository.SolicitacaoRepository;
import com.api.model.Solicitacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SolicitacaoService {
    
    @Autowired
    private SolicitacaoRepository repository;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Transactional
    public SolicitacaoResponse processarNovaSolicitacao(SolicitacaoRequest request) {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setCep(request.getCep());

        repository.save(solicitacao);
        log.info("Protocolo gerado: "+solicitacao.getProtocolo());
        request.setProtocolo(solicitacao.getProtocolo());


        enviarParaFilaProcessamento(request);

        return new SolicitacaoResponse(solicitacao.getProtocolo());
    }

    private void enviarParaFilaProcessamento(SolicitacaoRequest request) {
        rabbitTemplate.convertAndSend("solicitacao.exchange", "solicitacao.routingkey", request);
    }

    @Transactional(readOnly = true)
    public Solicitacao buscarPorProtocolo(String protocolo) {
        return repository.findByProtocolo(protocolo).orElseThrow();
    }
}