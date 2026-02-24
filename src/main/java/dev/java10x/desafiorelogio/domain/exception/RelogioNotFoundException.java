package dev.java10x.desafiorelogio.domain.exception;

import java.util.UUID;

public class RelogioNotFoundException extends RuntimeException {

    private final UUID uuid;

    public RelogioNotFoundException(UUID uuid) {
        super("Relógio não encontrado: " + uuid);
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

}
