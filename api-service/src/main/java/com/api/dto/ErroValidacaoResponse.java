package com.api.dto;

import org.springframework.validation.ObjectError;
import java.util.List;

public class ErroValidacaoResponse {
    private String mensagem;
    private List<String> erros;

    public ErroValidacaoResponse(String mensagem, List<ObjectError> erros) {
        this.mensagem = mensagem;
        this.erros = erros.stream().map(ObjectError::getDefaultMessage).toList();
    }

    public String getMensagem() {
        return mensagem;
    }

    public List<String> getErros() {
        return erros;
    }
}
