package dev.java10x.desafiorelogio.domain.enums;

public enum TipoVidroEnum {
    MINERAL, SAFIRA, ACRILICO;

    public static TipoVidroEnum fromApi(String valor) {
        if (valor == null || valor.isBlank())
            return null;
        return switch (valor.toLowerCase()) {
            case "mineral" -> MINERAL;
            case "safira" -> SAFIRA;
            case "acrilico" -> ACRILICO;
            default -> throw new IllegalArgumentException("Valor inválido para tipo do vidro: " + valor);
        };
    }

    public String toApi() {
        return switch (this) {
            case MINERAL -> "mineral";
            case SAFIRA -> "safira";
            case ACRILICO -> "acrilico";
        };
    }
}
