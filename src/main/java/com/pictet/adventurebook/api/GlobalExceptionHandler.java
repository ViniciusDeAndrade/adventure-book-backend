package com.pictet.adventurebook.api;

import com.pictet.adventurebook.exception.GameOverException;
import com.pictet.adventurebook.exception.InvalidBookException;
import com.pictet.adventurebook.exception.InvalidChoiceException;
import com.pictet.adventurebook.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException e) {
        log.warn(messageSource.getMessage("log.exception.notFound", new Object[]{e.getMessage()}, Locale.getDefault()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<Map<String, String>> handleInvalidBook(InvalidBookException e) {
        log.warn(messageSource.getMessage("log.exception.invalidBook", new Object[]{e.getMessage()}, Locale.getDefault()));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(InvalidChoiceException.class)
    public ResponseEntity<Map<String, String>> handleInvalidChoice(InvalidChoiceException e) {
        log.warn(messageSource.getMessage("log.exception.invalidChoice", new Object[]{e.getMessage()}, Locale.getDefault()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(GameOverException.class)
    public ResponseEntity<Map<String, String>> handleGameOver(GameOverException e) {
        log.warn(messageSource.getMessage("log.exception.gameOver", new Object[]{e.getMessage()}, Locale.getDefault()));
        return ResponseEntity.status(HttpStatus.GONE).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        String invalidMsg = messageSource.getMessage("validation.invalid", null, Locale.getDefault());
        var errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        err -> err.getDefaultMessage() != null ? err.getDefaultMessage() : invalidMsg,
                        (a, b) -> a));
        log.warn(messageSource.getMessage("log.exception.validation", new Object[]{errors}, Locale.getDefault()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", errors));
    }
}
