package com.api.controller;

import com.api.docs.SolicitacaoApiDoc;
import com.api.dto.ErroValidacaoResponse;
import com.api.dto.SolicitacaoRequest;
import com.api.dto.SolicitacaoResponse;
import com.api.model.Solicitacao;
import com.api.service.SolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController implements SolicitacaoApiDoc {

    private final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @Override
    @PostMapping
    public ResponseEntity<?> criarSolicitacao(@Valid @RequestBody SolicitacaoRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErroValidacaoResponse("Erro na validação", bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList() ));
        }

        SolicitacaoResponse response = solicitacaoService.processarNovaSolicitacao(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{protocolo}")
                .buildAndExpand(response.getProtocolo())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }



    @Override
    @GetMapping("/{protocolo}")
    public ResponseEntity<Solicitacao> consultarSolicitacao(@PathVariable String protocolo) {
        return ResponseEntity.ok(solicitacaoService.buscarPorProtocolo(protocolo));
    }
}
