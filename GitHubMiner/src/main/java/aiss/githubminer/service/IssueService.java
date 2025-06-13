package aiss.githubminer.service;


import aiss.githubminer.model.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public List<Issue> getIssues(String owner, String repo, Integer sinceIssues, Integer maxPages) {
        LocalDateTime now = LocalDateTime.now();
        String resultIssues = now.minusDays(sinceIssues).format(GitHubAPIService.formatter);
        return IntStream.rangeClosed(1, maxPages)
                .parallel()
                .mapToObj(page ->
                        gitHubAPIService.get("repos/{owner}/{repo}/issues?since={sinceIssues}&page={page}", Issue[].class, owner, repo, resultIssues, page))
                .flatMap(Arrays::stream)
                .peek(issue -> {if (issue.getAuthor() != null) issue.setAuthor(userService.getUser(issue.getAuthor().getUsername()));})
                .peek(issue -> {if (issue.getAssignee() != null) issue.setAssignee(userService.getUser(issue.getAssignee().getUsername()));})
                .peek(issue -> issue.setComments(commentService.getComments(owner, repo, issue.getNumber(), maxPages)))
                .collect(Collectors.toList());
    }
}
