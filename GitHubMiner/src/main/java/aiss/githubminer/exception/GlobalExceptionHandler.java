package aiss.githubminer.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GitHubMinerException.class)
    public ResponseEntity<LinkedHashMap<String, ?>> handleApiException(GitHubMinerException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatus());
    }
}
