package com.example.worker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespostaMessage {
    private String protocolo;
    private String dadosProcessados;
    private String status;
    private String mensagemErro;
}