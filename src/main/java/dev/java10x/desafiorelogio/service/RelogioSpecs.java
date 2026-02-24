package dev.java10x.desafiorelogio.service;

import org.springframework.data.jpa.domain.Specification;

import dev.java10x.desafiorelogio.domain.entity.RelogioEntity;
import dev.java10x.desafiorelogio.domain.enums.MaterialCaixaEnum;
import dev.java10x.desafiorelogio.domain.enums.TipoMovimentoEnum;
import dev.java10x.desafiorelogio.domain.enums.TipoVidroEnum;

public class RelogioSpecs {

    private RelogioSpecs() {
    }

    public static boolean blank(String s) {
        return s == null || s.isBlank();
    }

    public static Specification<RelogioEntity> tudo() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<RelogioEntity> busca(String termo) {
        if (blank(termo))
            return tudo();

        String like = "%" + termo.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("marca")), like),
                cb.like(cb.lower(root.get("modelo")), like),
                cb.like(cb.lower(root.get("referencia")), like));
    }

    public static Specification<RelogioEntity> marca(String marca) {
        if (blank(marca))
            return tudo();

        return (root, query, cb) -> cb.equal(cb.lower(root.get("marca")), marca);
    }

    public static Specification<RelogioEntity> tipoMovimento(TipoMovimentoEnum tipoMovimento) {
        if (tipoMovimento == null)
            return tudo();

        return (root, query, cb) -> cb.equal(root.get("tipoMovimento"), tipoMovimento);
    }

    public static Specification<RelogioEntity> tipoVidroIagual(TipoVidroEnum tipoVidro) {
        if (tipoVidro == null)
            return tudo();

        return (root, query, cb) -> cb.equal(root.get("tipoVidro"), tipoVidro);
    }

    public static Specification<RelogioEntity> materialCaixaIgual(MaterialCaixaEnum materialCaixa) {
        if (materialCaixa == null)
            return tudo();

        return (root, query, cb) -> cb.equal(root.get("materialCaixa"), materialCaixa);
    }

    public static Specification<RelogioEntity> resistenciaAguaEntre(Integer min, Integer max) {
        if (min == null && max == null)
            return tudo();

        return (root, query, cb) -> {
            if (min != null && max != null)
                return cb.between(root.get("resistenciaAguaM"), min, max);
            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("resistenciaAguaM"), min);
            return cb.lessThanOrEqualTo(root.get("resistenciaAguaM"), max);
        };
    }

    public static Specification<RelogioEntity> precoEntre(Long min, Long max) {
        if (min == null && max == null)
            return tudo();

        return (root, query, cb) -> {
            if (min != null && max != null)
                return cb.between(root.get("precoEmCentavos"), min, max);
            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("precoEmCentavos"), min);
            return cb.lessThanOrEqualTo(root.get("precoEmCentavos"), max);
        };
    }

    public static Specification<RelogioEntity> diametroEntre(Integer min, Integer max) {
        if (min == null && max == null)
            return tudo();

        return (root, query, cb) -> {
            if (min != null && max != null)
                return cb.between(root.get("diametroMm"), min, max);
            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("diametroMm"), min);
            return cb.lessThanOrEqualTo(root.get("diametroMm"), max);
        };
    }
}
