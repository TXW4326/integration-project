package aiss.gitminer.exception;

import aiss.gitminer.dto.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice //This notation is the same as @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {

    // Specific handlers by model:

    @ExceptionHandler(IssueNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIssueNotFoundException(IssueNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                "Not Found",
                "Requested Issue doesn't exists."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotFoundException(CommentNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                "Not Found",
                "Requested Comment doesn't exist."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommitNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommitNotFoundException(CommitNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                "Not Found",
                "Requested Commit doesn't exist."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProjectNotFoundException(ProjectNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                "Not Found",
                "Requested Project doesn't exist."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                "Not Found",
                "Requested User doesn't exist."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Validation errors handler:
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors =  ex.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "Request does not match validation constraints.",
                errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Generic handler for unexpected exceptions:
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex) {
        System.err.println("An uncontrolled error has occurred: " + ex.getMessage());
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error has occurred in the server. Please, try again later."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonMappingException(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "Error mapping JSON data: " + ex.getMostSpecificCause().getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
