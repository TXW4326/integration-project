package aiss.githubminer.service;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Comment;
import aiss.githubminer.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CommentService {

    private final GitHubAPIService gitHubAPIService;
    private final UserService userService;

    @Autowired
    public CommentService(GitHubAPIService gitHubAPIService,
                          UserService userService) {
        this.gitHubAPIService = gitHubAPIService;
        this.userService = userService;
    }


    private Comment[] handleCommentApiCall(String owner, String repo, Integer issueNumber, int page, Integer maxPages) {
        try {
            return gitHubAPIService.get("repos/{owner}/{repo}/issues/{issueNumber}/comments?page={page}", Comment[].class, owner, repo, issueNumber, page);
        } catch (HttpStatusCodeException e) {
            Map<String, ?> parameters = Map.of(
                    "owner", owner,
                    "repo", repo,
                    "issueNumber", issueNumber,
                    "maxPages", maxPages,
                    "page", page
            );
            switch (e.getStatusCode().value()) {
                case 404: throw new GitHubMinerException(HttpStatus.NOT_FOUND, Map.of(
                        "message", "Comment not found",
                        "parameters", parameters));
                case 410: throw new GitHubMinerException(HttpStatus.GONE, Map.of(
                        "message", "Comment has been deleted or is no longer available",
                        "parameters", parameters));
                default: throw  new GitHubMinerException(e.getStatusCode(), Map.of(
                        "message", "An error occurred while fetching comments",
                        "parameters", parameters));
            }
        } catch (UnknownHttpStatusCodeException e) {
            throw new GitHubMinerException(e.getStatusCode(), Map.of(
                    "message", "An unknown error occurred while fetching comments",
                    "parameters", Map.of(
                            "owner", owner,
                            "repo", repo,
                            "issueNumber", issueNumber,
                            "maxPages", maxPages,
                            "page", page
                    )
            ));
        } catch (RuntimeException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(
                    "message", "An error occurred while fetching comments",
                    "parameters", Map.of(
                            "owner", owner,
                            "repo", repo,
                            "issueNumber", issueNumber,
                            "maxPages", maxPages,
                            "page", page
                    )
            ));
        }
    }


    List<Comment> getCommentsInternal(String owner, String repo, Integer issueNumber, Integer maxPages) {
        return IntStream.rangeClosed(1, maxPages)
                .parallel()
                .mapToObj(page -> handleCommentApiCall(owner, repo, issueNumber, page, maxPages))
                .flatMap(Arrays::stream)
                .peek(comment -> {if (comment.getAuthor() != null) comment.setAuthor(userService.getUserInternal(comment.getAuthor().getUsername()));})
                .collect(Collectors.toList());
    }

    public List<Comment> getComments(String owner, String repo, Integer issueNumber, Integer maxPages) {
        userInputValidation(owner, repo, issueNumber, maxPages);
        return getCommentsInternal(owner, repo, issueNumber, maxPages);
    }

    private static void userInputValidation(String owner, String repo, Integer issueNumber, Integer maxPages) {
        ValidationUtils.validateOwnerAndRepo(owner, repo);
        ValidationUtils.validateIssueNumber(issueNumber);
        ValidationUtils.validateMaxPages(maxPages);
    }
}
