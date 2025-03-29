package com.api.handler;

import com.api.dto.SolicitacaoRequest;
import com.api.controller.SolicitacaoController;
import com.api.service.SolicitacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class GenericExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private SolicitacaoController solicitacaoController;

    @Mock
    private SolicitacaoService solicitacaoService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(solicitacaoController)
                .setControllerAdvice(new GenericExceptionHandler()) // Adiciona o handler de exceção
                .build();
    }

    @Test
    void testHandleValidationExceptions_DeveRetornarBadRequest() throws Exception {
        SolicitacaoRequest request = new SolicitacaoRequest();
        request.setCep("123");

        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/solicitacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cep\":\"123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Erro na validação"));
    }


    @Test
    void testHandleValidationExceptions_DeveRetornarBadRequest_ComErroGenerico() throws Exception {
        mockMvc.perform(post("/solicitacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cep\":\"1234\"}"))
                .andExpect(status().isBadRequest()) // Verifica o status 400
                .andExpect(jsonPath("$.mensagem").value("Erro na validação")); // Verifica a resposta genérica
    }

    @Test
    void testHandleValidationExceptions_DeveRetornarBadRequest_QuandoErroNull() throws Exception {
        // Executando o teste
        mockMvc.perform(post("/solicitacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cep\":\"\"}"))
                .andExpect(status().isBadRequest()) // Verifica o status 400
                .andExpect(jsonPath("$.mensagem").value("Erro na validação")); // Não deve haver erro
    }
}