package aiss.githubminer.exception;

import org.springframework.http.HttpStatusCode;

public class GitHubMinerException extends RuntimeException {
    private final HttpStatusCode status;
    private final Object reason;

    public GitHubMinerException(HttpStatusCode status, Object reason) {
        super("");
        this.status = status;
        this.reason = reason;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public Object getReason() {
        return reason;
    }
}
