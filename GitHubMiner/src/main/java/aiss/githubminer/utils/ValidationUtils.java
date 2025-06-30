package aiss.githubminer.utils;

import aiss.githubminer.exception.GitHubMinerException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;


// This class provides utility methods for validating user input parameters, avoiding redundant code in services.
// Spring doesn't really allow the user to pass empty or null parameters, but this class is also useful for validating parameters that are not directly passed by the user, such as in tests.
public final class ValidationUtils {

    private static final Validator validator = buildDefaultValidatorFactory().getValidator();

    private ValidationUtils() {}

    public static void validateOwnerAndRepo(String owner, String repo) {
        if (owner == null) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Owner is null");
        if (owner.isBlank()) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Owner is empty");
        if (repo == null) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Repository is null");
        if (repo.isBlank()) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Repository is empty");
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
    public static LinkedHashMap<String,?> validateGraphQLresponse(LinkedHashMap<String, ?> response, LinkedHashMap<String,?> parameters) {
        if (response == null) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(
                    "error", "An error occurred while fetching the data",
                    "parameters", parameters)
            );
        }
        if (response.containsKey("errors")) {
            if (response.get("errors") instanceof List<?>) {
                List<LinkedHashMap<String, ?>> errors = (List<LinkedHashMap<String, ?>>) response.get("errors");
                if (errors.size() == 1) {
                    LinkedHashMap<String, ?> error = errors.get(0);
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
                    throw new GitHubMinerException(status, LinkedHashMapBuilder.of()
                            .add("error", "An error occurred while fetching the data for the given parameters")
                            .add("parameters", parameters)
                            .add("description", error)
                    );
                }
                throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, LinkedHashMapBuilder.of()
                        .add("error", "Multiple errors occurred while fetching the data for the given parameters")
                        .add("parameters", parameters)
                        .add("errors", errors)
                );
            }
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, LinkedHashMapBuilder.of()
                    .add("error", "An error occurred while fetching the data for the given parameters")
                    .add("parameters", parameters)
                    .add("description", response.get("errors"))
            );
        }
        if (!response.containsKey("data") ||
                response.get("data") == null ||
                !(response.get("data") instanceof LinkedHashMap<?, ?> data) ||
                data.isEmpty() ||
                !data.containsKey("repository") ||
                data.get("repository") == null ||
                !(data.get("repository") instanceof LinkedHashMap<?, ?> repository) ||
                repository.keySet().stream().anyMatch(k -> !(k instanceof String))) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, LinkedHashMapBuilder.of()
                    .add("error", "No data found for the given parameters")
                    .add("parameters", parameters)
            );
        }

        return (LinkedHashMap<String, ?>) repository;
    }

    //Validates an object using the Jakarta Bean Validation API.
    public static <T> T validateObject(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (violations.isEmpty()) return object;
        throw new GitHubMinerException(HttpStatus.BAD_REQUEST, LinkedHashMapBuilder.of()
                .add("error", "Invalid " + object.getClass().getSimpleName() + " fetched data" )
                .add("violations", violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList())
                )
        );
    }
}
