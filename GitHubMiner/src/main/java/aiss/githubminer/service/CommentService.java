package aiss.githubminer.service;

import aiss.githubminer.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
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

    public List<Comment> getComments(String owner, String repo, Integer issueNumber, Integer maxPages) {
        return IntStream.rangeClosed(1, maxPages)
                .parallel()
                .mapToObj(page -> gitHubAPIService.get("repos/{owner}/{repo}/issues/{issueNumber}/comments?page={page}", Comment[].class, owner, repo, issueNumber, page))
                .flatMap(Arrays::stream)
                .peek(comment -> {if (comment.getAuthor() != null) comment.setAuthor(userService.getUser(comment.getAuthor().getUsername()));})
                .collect(Collectors.toList());
    }
}
