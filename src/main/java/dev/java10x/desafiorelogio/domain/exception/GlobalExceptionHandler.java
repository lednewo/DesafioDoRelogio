package dev.java10x.desafiorelogio.domain.exception;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RelogioNotFoundException.class)
    public ResponseEntity<ErrorApi> handleRelogioNotFound(RelogioNotFoundException ex, HttpServletRequest req) {
        ErrorApi error = new ErrorApi(
                Instant.now(),
                404,
                "Relógio não encontrado",
                ex.getMessage(),
                req.getRequestURI(),
                Collections.emptyList());
        return new ResponseEntity<ErrorApi>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorApi> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        ErrorApi error = new ErrorApi(
                Instant.now(),
                400,
                "Requisição inválida",
                ex.getMessage(),
                req.getRequestURI(),
                Collections.emptyList());
        return new ResponseEntity<ErrorApi>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpServletRequest req) {
        List<ErrorApi.ErrorDetails> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toErrorDetails)
                .toList();

        ErrorApi error = new ErrorApi(
                Instant.now(),
                400,
                "Falha de validação",
                "Um ou mais campos estão inválidos",
                req.getRequestURI(),
                details);

        return new ResponseEntity<ErrorApi>(error, HttpStatus.BAD_REQUEST);
    }

    private ErrorApi.ErrorDetails toErrorDetails(FieldError fieldError) {
        return new ErrorApi.ErrorDetails(fieldError.getField(), fieldError.getDefaultMessage());
    }
}