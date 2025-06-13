package aiss.githubminer.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GitHubMinerException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(GitHubMinerException ex) {
        Map<String,Object> map = new HashMap<>();
        map.put("errors", ex.getReason());
        return new ResponseEntity<>(map, ex.getStatus());
    }
}
