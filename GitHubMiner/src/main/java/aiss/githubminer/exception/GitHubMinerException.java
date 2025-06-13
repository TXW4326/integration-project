package aiss.githubminer.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class GitHubMinerException extends RuntimeException {
    private final HttpStatusCode status;
    private final Map<String, ?> reason;

    public GitHubMinerException(HttpStatusCode status, Object reason) {
        super(reason.toString());
        this.status = status;
        this.reason = Map.of("error", reason);
    }

    public GitHubMinerException(HttpStatusCode status, Map<String,?> reason) {
        super(reason.toString());
        this.status = status;
        this.reason = reason;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public Map<String,?> getReason() {
        return reason;
    }
}
