package dev.java10x.desafiorelogio.domain.dto.mapper;

import org.springframework.stereotype.Component;

import dev.java10x.desafiorelogio.domain.dto.RelogioResponse;
import dev.java10x.desafiorelogio.domain.entity.RelogioEntity;
import dev.java10x.desafiorelogio.domain.enums.MaterialCaixaEnum;
import dev.java10x.desafiorelogio.domain.enums.TipoMovimentoEnum;
import dev.java10x.desafiorelogio.domain.enums.TipoVidroEnum;

@Component
public class RelogioMapper {

    public RelogioResponse tRelogioResponse(RelogioEntity r) {
        return RelogioResponse.builder()
                .id(r.getId())
                .marca(r.getMarca())
                .modelo(r.getModelo())
                .referencia(r.getReferencia())
                .tipoMovimento(r.getTipoMovimento().toApi())
                .materialCaixa(r.getMaterialCaixa().toApi())
                .tipoVidro(r.getTipoVidro().toApi())
                .resistenciaAguaM(r.getResistenciaAguaM())
                .diametroMm(r.getDiametroMm())
                .lugtoLugMm(r.getLugToLugMm())
                .espessuraMm(r.getEspessuraMm())
                .larguraMm(r.getLarguraLugMm())
                .precoEmCentavos(r.getPrecoEmCentavos())
                .urlImagem(r.getUrlImagem())
                .etiquetaResistenciaAgua(etiquetaResistencia(r.getResistenciaAguaM()))
                .pontuacaoColecionador(pontuacaoColecionador(r))
                .build();
    }

    private String etiquetaResistencia(int resistenciaM) {
        if (resistenciaM < 50)
            return "respingos";
        if (resistenciaM < 100)
            return "uso_diario";
        if (resistenciaM < 200)
            return "natacao";
        return "mergulho";
    }

    private int pontuacaoColecionador(RelogioEntity r) {
        int pontuacao = 0;
        if (r.getTipoVidro() == TipoVidroEnum.SAFIRA)
            pontuacao += 25;
        if (r.getResistenciaAguaM() >= 100)
            pontuacao += 15;
        if (r.getResistenciaAguaM() >= 200)
            pontuacao += 10;
        if (r.getTipoMovimento() == TipoMovimentoEnum.AUTOMATICO)
            pontuacao += 20;
        if (r.getMaterialCaixa() == MaterialCaixaEnum.ACO)
            pontuacao += 10;
        if (r.getMaterialCaixa() == MaterialCaixaEnum.TITANIO)
            pontuacao += 12;
        if (r.getDiametroMm() >= 38 && r.getDiametroMm() <= 42)
            pontuacao += 8;
        return pontuacao;
    }

}
