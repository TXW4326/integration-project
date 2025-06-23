package aiss.githubminer.service;


import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Issue;
import aiss.githubminer.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class IssueService {

    private final GitHubAPIService gitHubAPIService;
    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public IssueService(GitHubAPIService gitHubAPIService,
                        UserService userService,
                        CommentService commentService) {
        this.gitHubAPIService = gitHubAPIService;
        this.userService = userService;
        this.commentService = commentService;
    }


    private Issue[] handleIssueApiCall(String owner, String repo, String resultIssues, int page, int maxPages, int sinceIssues) {
        try {
            return gitHubAPIService.get("repos/{owner}/{repo}/issues?since={sinceIssues}&page={page}", Issue[].class, owner, repo, resultIssues, page);
        } catch (HttpStatusCodeException e) {
            Map<String, ?> parameters = Map.of(
                    "owner", owner,
                    "repo", repo,
                    "sinceIssues", sinceIssues,
                    "maxPages", maxPages,
                    "page", page
            );
            switch (e.getStatusCode().value()) {
                case 404: throw new GitHubMinerException(HttpStatus.NOT_FOUND, Map.of(
                        "error", "No issues found for the given parameters",
                        "parameters", parameters));
                case 301: throw new GitHubMinerException(HttpStatus.MOVED_PERMANENTLY, Map.of(
                        "error", "Issues have been moved",
                        "parameters", parameters));
                case 422: throw new GitHubMinerException(HttpStatus.UNPROCESSABLE_ENTITY, Map.of(
                        "error", "Unprocessable entity for issue: Validation failed, or the endpoint has been spammed.",
                        "parameters", parameters));
                default: throw new GitHubMinerException(e.getStatusCode(), Map.of(
                        "error", "An error occurred while fetching issues",
                        "parameters", parameters));
            }
        } catch (UnknownHttpStatusCodeException e) {
            throw new GitHubMinerException(e.getStatusCode(), Map.of(
                    "error", "An unknown error occurred while fetching issues",
                    "parameters", Map.of(
                            "owner", owner,
                            "repo", repo,
                            "sinceIssues", sinceIssues,
                            "maxPages", maxPages,
                            "page", page
                    )
            ));
        } catch (RuntimeException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(
                    "error", "An error occurred while fetching issues",
                    "parameters", Map.of(
                            "owner", owner,
                            "repo", repo,
                            "sinceIssues", sinceIssues,
                            "maxPages", maxPages,
                            "page", page
                    )
            ));
        }
    }
    List<Issue> getIssuesInternal(String owner, String repo, int sinceIssues, int maxPages) {
        LocalDateTime now = LocalDateTime.now();
        String resultIssues = now.minusDays(sinceIssues).format(GitHubAPIService.formatter);
        List<Issue> issues = new ArrayList<>();
        for (int page = 1; page <= maxPages; page++) {
            Issue[] issuesArray = handleIssueApiCall(owner, repo, resultIssues, page, maxPages, sinceIssues);
            issues.addAll(Stream.of(issuesArray).parallel()
                    .peek(issue -> {if (issue.getAuthor() != null) issue.setAuthor(userService.getUserInternal(issue.getAuthor().getUsername()));})
                    .peek(issue -> {if (issue.getAssignee() != null) issue.setAssignee(userService.getUserInternal(issue.getAssignee().getUsername()));})
                    .peek(issue -> issue.setComments(commentService.getCommentsInternal(owner, repo, issue.getNumber(), maxPages)))
                    .toList()
            );
            if (issuesArray.length < gitHubAPIService.PER_PAGE) break;
        }
        return issues;
    }

    // Method implemented in case it is needed to get issues without getting the project first
    public List<Issue> getIssues(String owner, String repo, int sinceIssues, int maxPages) {
        userInputValidation(owner, repo, sinceIssues, maxPages);
        return getIssuesInternal(owner, repo, sinceIssues, maxPages);
    }

    private static void userInputValidation(String owner, String repo, int sinceIssues, int maxPages) {
        ValidationUtils.validateOwnerAndRepo(owner, repo);
        ValidationUtils.validateSinceIssues(sinceIssues);
        ValidationUtils.validateMaxPages(maxPages);
    }
}
