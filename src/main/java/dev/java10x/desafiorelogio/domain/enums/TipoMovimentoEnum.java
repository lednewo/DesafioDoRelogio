package dev.java10x.desafiorelogio.domain.enums;

public enum TipoMovimentoEnum {
    QUARTZ, AUTOMATICO, MANUAL;

    public static TipoMovimentoEnum fromApi(String valor) {
        if (valor == null || valor.isBlank())
            return null;
        return switch (valor.toLowerCase()) {
            case "quartz" -> QUARTZ;
            case "automatic" -> AUTOMATICO;
            case "manual" -> MANUAL;
            default -> throw new IllegalArgumentException("Valor inválido para tipo de movimento: " + valor);
        };
    }

    public String toApi() {
        return switch (this) {
            case QUARTZ -> "quartz";
            case AUTOMATICO -> "automatico";
            case MANUAL -> "manual";
        };
    }
}
