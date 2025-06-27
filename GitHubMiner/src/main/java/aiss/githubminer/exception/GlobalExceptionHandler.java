package aiss.githubminer.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GitHubMinerException.class)
    public ResponseEntity<LinkedHashMap<String, ?>> handleApiException(GitHubMinerException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatus());
    }
}
