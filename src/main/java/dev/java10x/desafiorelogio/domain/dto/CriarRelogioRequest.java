package dev.java10x.desafiorelogio.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CriarRelogioRequest(
                @NotBlank @Size(max = 80) String marca,
                @NotBlank @Size(max = 120) String modelo,
                @NotBlank @Size(max = 80) String referencia,

                @NotBlank String tipoMovimento,
                @NotBlank String materialCaixa,
                @NotBlank String tipoVidro,

                @Min(0) int resistenciaAguaM,
                @Min(20) int diametroMm,
                @Min(20) int lugtoLugMm,
                @Min(5) int espessuraMm,
                @Min(10) int larguraMm,
                @Min(1) Long precoEmCentavos,
                @NotNull @Size(max = 600) String urlImagem) {

}
