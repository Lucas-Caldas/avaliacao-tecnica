package com.example.worker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolicitacaoMessage {
    private String protocolo;
    private String cep;

    @Override
    public String toString() {
        return "SolicitacaoMessage{" +
                "protocolo='" + protocolo + '\'' +
                ", cep='" + cep + '\'' +
                '}';
    }
}