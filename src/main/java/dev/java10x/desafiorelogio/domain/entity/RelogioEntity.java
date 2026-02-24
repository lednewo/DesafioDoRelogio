package dev.java10x.desafiorelogio.domain.entity;

import java.time.Instant;
import java.util.UUID;

import dev.java10x.desafiorelogio.domain.enums.MaterialCaixaEnum;
import dev.java10x.desafiorelogio.domain.enums.TipoMovimentoEnum;
import dev.java10x.desafiorelogio.domain.enums.TipoVidroEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "relogio", indexes = {
        @Index(name = "IDX_RELOGIO_MARCA", columnList = "marca"),
        @Index(name = "IDX_RELOGIO_CRIADO_EM", columnList = "criadoEm"),
        @Index(name = "IDX_RELOGIO_PRECO", columnList = "precoEmCentavos"),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelogioEntity {

    @Id
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false, length = 80)
    private String marca;

    @Column(nullable = false, length = 125)
    private String modelo;

    @Column(nullable = false, length = 80)
    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoMovimentoEnum tipoMovimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MaterialCaixaEnum materialCaixa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoVidroEnum tipoVidro;

    @Column(nullable = false)
    private int resistenciaAguaM;

    @Column(nullable = false)
    private int diametroMm;

    @Column(nullable = false)
    private int lugToLugMm;

    @Column(nullable = false)
    private int espessuraMm;

    @Column(nullable = false)
    private int larguraLugMm;

    @Column(nullable = false)
    private Long precoEmCentavos;

    @Column(nullable = false, length = 600)
    private String urlImagem;

    @Column(nullable = false)
    private Instant criadoEm;

    @PrePersist
    void prePersist() {
        if (id == null)
            id = UUID.randomUUID();
        if (criadoEm == null)
            criadoEm = Instant.now();
        normalizar();
    }

    @PreUpdate
    void preUpdate() {
        normalizar();
    }

    private void normalizar() {
        if (marca != null)
            marca = marca.trim();
        if (modelo != null)
            modelo = modelo.trim();
        if (referencia != null)
            referencia = referencia.trim();
        if (urlImagem != null)
            urlImagem = urlImagem.trim();
    }
}
