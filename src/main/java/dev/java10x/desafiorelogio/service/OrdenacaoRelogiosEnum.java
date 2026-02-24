package dev.java10x.desafiorelogio.service;

public enum OrdenacaoRelogiosEnum {
    MAIS_RECENTES,
    PRECO_CRESC,
    PRECO_DESC,
    DIAMETRO_CRESC,
    RESISTENCIA_DESC;

    public static OrdenacaoRelogiosEnum fromApi(String valor) {
        if (valor == null || valor.isBlank()) {
            return MAIS_RECENTES; // Valor padrão
        }
        return switch (valor) {
            case "newest" -> MAIS_RECENTES;
            case "price_asc" -> PRECO_CRESC;
            case "price_desc" -> PRECO_DESC;
            case "diameter_asc" -> DIAMETRO_CRESC;
            case "ar_desc" -> RESISTENCIA_DESC;
            default -> throw new IllegalArgumentException("Valor de ordenação inválido: " + valor);
        };
    }
}
