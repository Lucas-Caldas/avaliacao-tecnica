package com.example.worker.service;

import com.example.worker.dto.RespostaMessage;
import com.example.worker.dto.SolicitacaoMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Slf4j
public class ProcessadorService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${api.externa.url}")
    private String apiExternaUrl;

    public void processarSolicitacao(SolicitacaoMessage mensagem) {
        log.info("Menssagem de solicitação recebida.");
        log.debug("Mensagem lida: "+ mensagem.toString());
        WebClient.create()
                .get()
                .uri(apiExternaUrl + mensagem.getCep() + "/json")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(
                        resultado -> enviarResposta(mensagem, resultado, "PROCESSADO"),
                        erro -> enviarErro(mensagem, erro.getMessage())
                );
        log.info("Menssagem de solicitação processada.");
    }

    private void enviarResposta(SolicitacaoMessage mensagem, String dados, String status) {
        log.info("Mensagem para enviar Resposta");
        RespostaMessage resposta = new RespostaMessage();
        resposta.setProtocolo(mensagem.getProtocolo());
        resposta.setDadosProcessados(dados);
        resposta.setStatus("PROCESSADO");

        rabbitTemplate.convertAndSend(
                "solicitacao.exchange",
                "resposta.routingkey",
                resposta
        );
    }

    private void enviarErro(SolicitacaoMessage mensagem, String mensagemErro) {
        log.info("Menssagem pra enviar resposta erro.");
        RespostaMessage resposta = new RespostaMessage();
        resposta.setProtocolo(mensagem.getProtocolo());
        resposta.setStatus("ERRO");
        resposta.setMensagemErro(mensagemErro);

        rabbitTemplate.convertAndSend(
                "solicitacao.exchange",
                "resposta.routingkey",
                resposta
        );
    }
}