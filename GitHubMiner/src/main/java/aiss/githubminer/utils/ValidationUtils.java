package aiss.githubminer.utils;

import aiss.githubminer.exception.GitHubMinerException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static void validateOwnerAndRepo(String owner, String repo) {
        if (owner == null) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Owner is null");
        if (owner.isEmpty()) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Owner is empty");
        if (repo == null) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Repository is null");
        if (repo.isEmpty()) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Repository is empty");
    }

    public static void validateSinceCommits(int sinceCommits) {
        if (sinceCommits < 0) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "sinceCommits value cannot be negative: " + sinceCommits);
    }

    public static void validateSinceIssues(int sinceIssues) {
        if (sinceIssues < 0) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "sinceIssues value cannot be negative: " + sinceIssues);
    }

    public static void validateMaxPages(int maxPages) {
        if (maxPages <= 0) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "maxPages value cannot be negative: " + maxPages);
    }

    @SuppressWarnings("unchecked")
    public static Map<String,?> validateGraphQLresponse(Map<String, ?> response, Map<String,?> parameters) {
        if (response == null) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(
                    "error", "An error occurred while fetching the data",
                    "parameters", parameters)
            );
        }
        if (response.containsKey("errors")) {
            if (response.get("errors") instanceof List<?>) {
                List<Map<String, ?>> errors = (List<Map<String, ?>>) response.get("errors");
                if (errors.size() == 1) {
                    Map<String, ?> error = errors.get(0);
                    HttpStatus status;
                    if (error.containsKey("type") && error.get("type") instanceof String type) {
                        status = switch (type) {
                            case "NOT_FOUND" -> HttpStatus.NOT_FOUND;
                            case "FORBIDDEN" -> HttpStatus.FORBIDDEN;
                            case "UNAUTHORIZED" -> HttpStatus.UNAUTHORIZED;
                            case "RATE_LIMITED" -> HttpStatus.TOO_MANY_REQUESTS;
                            default -> HttpStatus.INTERNAL_SERVER_ERROR;
                        };
                    } else {
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                    }
                    throw new GitHubMinerException(status, Map.of(
                            "error", "An error occurred while fetching the data for the given parameters",
                            "parameters", parameters,
                            "description", error
                    ));
                }
                throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(
                        "error", "Multiple errors occurred while fetching the data for the given parameters",
                        "parameters", parameters,
                        "errors", errors
                ));
            }
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(
                "error", "An error occurred while fetching the data for the given parameters",
                "parameters", parameters,
                "description", response.get("errors")
            ));
    }
        if (!response.containsKey("data") ||
                response.get("data") == null ||
                !(response.get("data") instanceof Map<?,?> data) ||
                data.isEmpty() ||
                !data.containsKey("repository") ||
                data.get("repository") == null ||
                !(data.get("repository") instanceof Map<?,?> repository) ||
                repository.keySet().stream().anyMatch(k -> !(k instanceof String))) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(
                    "error", "No data found for the given parameters",
                    "parameters", parameters
            ));
        }

        return (Map<String,?>) repository;
    }
}
