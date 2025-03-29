package com.api.docs;

import com.api.dto.ErroValidacaoResponse;
import com.api.dto.SolicitacaoRequest;
import com.api.dto.SolicitacaoResponse;
import com.api.model.Solicitacao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface SolicitacaoApiDoc {

    @Operation(summary = "Criar nova solicitação", description = "Cria uma nova solicitação para busca de informações de endereço com base no CEP.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitação criada com sucesso",
                    content = @Content(schema = @Schema(implementation = SolicitacaoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ErroValidacaoResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<?> criarSolicitacao(SolicitacaoRequest request, BindingResult bindingResult);

    @Operation(summary = "Consultar solicitação", description = "Consulta uma solicitação pelo protocolo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação encontrada",
                    content = @Content(schema = @Schema(implementation = Solicitacao.class))),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    ResponseEntity<Solicitacao>  consultarSolicitacao(
            @Parameter(description = "Número do protocolo da solicitação", example = "PRT12345678", required = true)
            String protocolo);
}
