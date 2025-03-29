package com.api.dto;

import lombok.Data;

@Data
public class SolicitacaoResponse {
    public SolicitacaoResponse(String protocolo) {
        this.protocolo = protocolo;
    }
    private String protocolo;
    private String mensagem = "Solicitação recebida e em processamento";
}