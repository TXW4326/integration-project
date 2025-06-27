package aiss.gitminer.exception;

public class PullRequestNotFoundException extends RuntimeException {
    public PullRequestNotFoundException(String message) {
        super(message);
    }
}
