package com.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespostaMessage {
    private String protocolo;
    private String dadosProcessados;
    private String status;
    private String mensagemErro;


    // Construtor para sucesso
    public RespostaMessage(String protocolo, String dadosProcessados) {
        this.protocolo = protocolo;
        this.dadosProcessados = dadosProcessados;
        this.status = "PROCESSADO";
    }

    // Construtor para erro
    public RespostaMessage(String protocolo, String status, String mensagemErro) {
        this.protocolo = protocolo;
        this.status = status;
        this.mensagemErro = mensagemErro;
    }
}