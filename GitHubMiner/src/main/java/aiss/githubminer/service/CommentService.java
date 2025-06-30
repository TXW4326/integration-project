package aiss.githubminer.service;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Comment;
import aiss.githubminer.utils.LinkedHashMapBuilder;
import aiss.githubminer.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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


    private Comment[] handleCommentApiCall(String owner, String repo, int issueNumber, int page, int maxPages) {
        try {
            return gitHubAPIService.get("repos/{owner}/{repo}/issues/{issueNumber}/comments?page={page}", Comment[].class, owner, repo, issueNumber, page);
        } catch (HttpStatusCodeException e) {
            LinkedHashMapBuilder parameters = LinkedHashMapBuilder.of()
                    .add("owner", owner)
                    .add("repo", repo)
                    .add("issueNumber", issueNumber)
                    .add("maxPages", maxPages)
                    .add("page", page);
            switch (e.getStatusCode().value()) {
                case 404: throw new GitHubMinerException(HttpStatus.NOT_FOUND, LinkedHashMapBuilder.of()
                        .add("error", "Comments not found")
                        .add("parameters", parameters));
                case 410: throw new GitHubMinerException(HttpStatus.GONE, LinkedHashMapBuilder.of()
                        .add("error", "Comments have been deleted or are no longer available")
                        .add("parameters", parameters));
                default: throw  new GitHubMinerException(e.getStatusCode(), LinkedHashMapBuilder.of()
                        .add("error", "An error occurred while fetching comments")
                        .add("parameters", parameters));
            }
        } catch (UnknownHttpStatusCodeException e) {
            throw new GitHubMinerException(e.getStatusCode(), LinkedHashMapBuilder.of()
                    .add("error", "An unknown error occurred while fetching comments")
                    .add("parameters", LinkedHashMapBuilder.of()
                            .add("owner", owner)
                            .add("repo", repo)
                            .add("issueNumber", issueNumber)
                            .add("maxPages", maxPages)
                            .add("page", page)
                    )
            );
        } catch (RuntimeException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, LinkedHashMapBuilder.of()
                    .add("error", "An error occurred while fetching comments")
                    .add("parameters", LinkedHashMapBuilder.of()
                            .add("owner", owner)
                            .add("repo", repo)
                            .add("issueNumber", issueNumber)
                            .add("maxPages", maxPages)
                            .add("page", page)
                    )
            );
        }
    }


    List<Comment> getCommentsInternal(String owner, String repo, int issueNumber, int maxPages) {
        List<Comment> comments = new ArrayList<>();
        for (int page = 1; page <= maxPages; page++) {
            Comment[] commentsArray = handleCommentApiCall(owner, repo, issueNumber, page, maxPages);
            comments.addAll(Stream.of(commentsArray).parallel()
                    .peek(comment -> {if (comment.getAuthor() != null) comment.setAuthor(userService.getUserInternal(comment.getAuthor().getUsername()));})
                    .toList()
            );
            if (commentsArray.length < gitHubAPIService.PER_PAGE) break;
        }
        return comments;
    }

    // Method implemented in case it is needed to get commits without getting the issue first. (For example, in testing)
    public List<Comment> getComments(String owner, String repo, int issueNumber, int maxPages) {
        userInputValidation(owner, repo, issueNumber, maxPages);
        return ValidationUtils.validateObject(getCommentsInternal(owner, repo, issueNumber, maxPages));
    }

    private static void userInputValidation(String owner, String repo, int issueNumber, int maxPages) {
        ValidationUtils.validateOwnerAndRepo(owner, repo);
        ValidationUtils.validateIssueNumber(issueNumber);
        ValidationUtils.validateMaxPages(maxPages);
    }
}
