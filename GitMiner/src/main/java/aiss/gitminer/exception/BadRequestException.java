package aiss.gitminer.exception;

import java.util.List;

public class BadRequestException extends RuntimeException {

    private List<String> details = null;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, List<String> details) {
        super(message);
        this.details = details;
    }

    public List<String> getDetails() {
        return details;
    }
}
