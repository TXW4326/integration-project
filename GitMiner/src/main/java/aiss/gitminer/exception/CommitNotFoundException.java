package aiss.gitminer.exception;

public class CommitNotFoundException extends RuntimeException {
    public CommitNotFoundException(String message) {
        super(message);
    }
}
