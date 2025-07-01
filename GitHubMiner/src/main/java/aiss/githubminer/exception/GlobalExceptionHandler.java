package aiss.githubminer.exception;


import aiss.githubminer.utils.LinkedHashMapBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GitHubMinerException.class)
    public ResponseEntity<LinkedHashMap<String, ?>> handleApiException(GitHubMinerException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<LinkedHashMap<String, ?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
                LinkedHashMapBuilder.of()
                        .add("error", "An error occurred while serializing JSON data")
                        .add("message", ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
