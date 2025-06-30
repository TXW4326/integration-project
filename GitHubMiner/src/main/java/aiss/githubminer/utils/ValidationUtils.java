package aiss.githubminer.utils;

import aiss.githubminer.exception.GitHubMinerException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;

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

    public static void validateIssueNumber(int issueNumber) {
        if (issueNumber <= 0) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Issue number cannot be negative:  " + issueNumber);
    }

    public static void validateUsername(String username) {
        if (username == null) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Username is null");
        if (username.isBlank()) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Username is empty");
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
