package com.api.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String protocolo;

    private String cep;
    @Column(columnDefinition = "TEXT")
    private String dadosApiExterna;

    @Column(nullable = false)
    private String status;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void prePersist() {
        this.protocolo = UUID.randomUUID().toString();
        this.status = "PENDENTE";
        this.dataCriacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}
