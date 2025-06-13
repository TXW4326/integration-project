package aiss.githubminer.utils;

import aiss.githubminer.exception.GitHubMinerException;
import org.springframework.http.HttpStatus;

public interface ValidationUtils {

    public static void validateOwnerAndRepo(String owner, String repo) {
        if (owner == null || owner.isEmpty()) {
            throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Owner is null or empty: " + owner);
        }
        if (repo == null || repo.isEmpty()) {
            throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Repository is null or empty: " + repo);
        }
    }

    public static void validateSinceCommits(Integer sinceCommits) {
        if (sinceCommits == null || sinceCommits < 0) {
            throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Invalid sinceCommits value: " + sinceCommits);
        }
    }

    public static void validateSinceIssues(Integer sinceIssues) {
        if (sinceIssues == null || sinceIssues < 0) {
            throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Invalid sinceIssues value: " + sinceIssues);
        }
    }

    public static void validateMaxPages(Integer maxPages) {
        if (maxPages == null || maxPages <= 0) {
            throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Invalid maxPages value: " + maxPages);
        }
    }

    public static void validateIssueNumber(Integer issueNumber) {
        if (issueNumber == null || issueNumber <= 0) {
            throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Invalid issue number: " + issueNumber);
        }
    }

    public static void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new GitHubMinerException(HttpStatus.BAD_REQUEST, "Username is null or empty");
        }
    }
}
