package aiss.githubminer.exception;

import aiss.githubminer.utils.JsonUtils;
import aiss.githubminer.utils.LinkedHashMapBuilder;
import org.springframework.http.HttpStatusCode;

import java.util.LinkedHashMap;

public class GitHubMinerException extends RuntimeException {
    private final HttpStatusCode status;
    private final LinkedHashMap<String, ?> reason;

    public GitHubMinerException(HttpStatusCode status, Object reason) {
        super(reason.toString());
        this.status = status;
        this.reason = LinkedHashMapBuilder.of().add("error", reason);
    }

    public GitHubMinerException(HttpStatusCode status, LinkedHashMap<String,?> reason) {
        super(JsonUtils.toJson(reason));
        this.status = status;
        this.reason = reason;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public LinkedHashMap<String,?> getReason() {
        return reason;
    }
}
