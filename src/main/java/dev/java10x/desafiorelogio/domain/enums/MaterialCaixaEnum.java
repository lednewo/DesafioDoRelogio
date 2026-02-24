package dev.java10x.desafiorelogio.domain.enums;

public enum MaterialCaixaEnum {

    ACO, TITANIO, RESINA, BRONZE, CERAMICA;

    public static MaterialCaixaEnum fromApi(String valor) {
        if (valor == null || valor.isBlank())
            return null;
        return switch (valor.toLowerCase()) {
            case "steel" -> ACO;
            case "titanium" -> TITANIO;
            case "resin" -> RESINA;
            case "bronze" -> BRONZE;
            case "ceramic" -> CERAMICA;
            default -> throw new IllegalArgumentException("Valor inválido para material da caixa: " + valor);
        };
    }

    public String toApi() {
        return switch (this) {
            case ACO -> "aço";
            case TITANIO -> "titânio";
            case RESINA -> "resina";
            case BRONZE -> "bronze";
            case CERAMICA -> "cerâmica";
        };
    }
}
