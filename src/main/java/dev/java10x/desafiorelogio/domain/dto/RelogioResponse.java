package dev.java10x.desafiorelogio.domain.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record RelogioResponse(
        UUID id,
        String marca,
        String modelo,
        String referencia,
        String tipoMovimento,
        String materialCaixa,
        String tipoVidro,
        int resistenciaAguaM,
        int diametroMm,
        int lugtoLugMm,
        int espessuraMm,
        int larguraMm,
        Long precoEmCentavos,
        String urlImagem,
        String etiquetaResistenciaAgua,
        int pontuacaoColecionador) {
}
