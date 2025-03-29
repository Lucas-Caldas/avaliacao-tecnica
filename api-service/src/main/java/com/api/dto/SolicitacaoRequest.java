package com.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolicitacaoRequest {
    @NotEmpty(message = "CEP é obrigatório.")
    @Size(min = 8, max = 8, message = "CEP deve conter exatamente 8 caracteres.")
    @Pattern(regexp = "^[0-9]{8}$", message = "CEP deve conter exatamente 8 dígitos numéricos sem caracteres especiais.")
    private String cep;
    private String protocolo;
}