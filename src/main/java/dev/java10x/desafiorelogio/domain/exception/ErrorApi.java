package dev.java10x.desafiorelogio.domain.exception;

import java.time.Instant;
import java.util.List;

public record ErrorApi(
        Instant timestamp,
        int status,
        String erro,
        String message,
        String path,
        List<ErrorDetails> errors

) {
    public record ErrorDetails(
            String field,
            String message) {
    }

}
