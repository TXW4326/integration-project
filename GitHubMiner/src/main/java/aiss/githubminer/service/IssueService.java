package aiss.githubminer.service;


import aiss.githubminer.model.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class IssueService {

    @Autowired
    GitHubAPIService gitHubAPIService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public List<Issue> getIssues(String owner, String repo, Integer sinceIssues, Integer maxPages) {
        LocalDateTime now = LocalDateTime.now();
        String resultIssues = now.minusDays(sinceIssues).format(GitHubAPIService.formatter);
        return IntStream.rangeClosed(1, maxPages)
                .mapToObj(page ->
                        gitHubAPIService.get("repos/{owner}/{repo}/issues?since={sinceIssues}&page={page}", Issue[].class, owner, repo, resultIssues, page))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }
}
