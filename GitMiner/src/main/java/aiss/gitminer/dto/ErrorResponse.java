package aiss.gitminer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        name = "ErrorResponse",
        description = "Standard error response format"
)
public class ErrorResponse {
    @Schema(description = "Timestamp of the error occurrence", example = "2023-10-01T12:00:00Z", required = true)
    private final LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "404", required = true)
    private final int status;

    @Schema(description = "Error type", example = "Not Found", required = true)
    private final String error;

    @Schema(description = "Detailed error message", example = "The requested resource was not found", required = true)
    private final String message;

    @Schema(description = "List of details about the error", example = "[\"Detail 1\", \"Detail 2\"]")
    private final List<String> details;

    public ErrorResponse(HttpStatus status, String error, String message, List<String> details) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = error;
        this.message = message;
        this.details = details;
    }

    public ErrorResponse(HttpStatus status, String error, String message) {
        this(status, error, message, null);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }
}
