package com.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.ObjectError;
import java.util.List;

@AllArgsConstructor
@Getter
public class ErroValidacaoResponse {
    private String mensagem;
    private List<String> erros;
}
