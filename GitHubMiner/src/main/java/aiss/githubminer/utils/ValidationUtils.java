package aiss.githubminer.utils;

import aiss.githubminer.exception.GitHubMinerException;
import org.springframework.http.HttpStatus;

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

    public static void validateIssueNumber(int issueNumber) {
        if (issueNumber <= 0) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Issue number cannot be negative:  " + issueNumber);
    }

    public static void validateUsername(String username) {
        if (username == null) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Username is null");
        if (username.isEmpty()) throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Username is empty");
    }
}
