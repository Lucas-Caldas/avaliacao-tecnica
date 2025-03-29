package com.api.controller;

import com.api.dto.ErroValidacaoResponse;
import com.api.dto.SolicitacaoRequest;
import com.api.dto.SolicitacaoResponse;
import com.api.service.SolicitacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SolicitacaoControllerTest {

    @Mock
    private SolicitacaoService solicitacaoService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private SolicitacaoController solicitacaoController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(solicitacaoController).build();
    }

    @Test
    void testCriarSolicitacao_ComDadosValidos_DeveRetornarCreated() throws Exception {
        SolicitacaoRequest request = new SolicitacaoRequest();
        request.setCep("70722500");

        SolicitacaoResponse mockResponse = new SolicitacaoResponse("12345");

        when(solicitacaoService.processarNovaSolicitacao(request)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/solicitacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cep\":\"70722500\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.protocolo").value("12345"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem").value("Solicitação recebida e em processamento"));
    }

    @Test
    void testCriarSolicitacao_ComErroDeValidacao_DeveRetornarBadRequest() {
        SolicitacaoRequest request = new SolicitacaoRequest();
        request.setCep("123");

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(new ObjectError("cep", "CEP inválido")));

        ResponseEntity<?> response = solicitacaoController.criarSolicitacao(request, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof ErroValidacaoResponse);


        verify(solicitacaoService, never()).processarNovaSolicitacao(any());
    }

}
