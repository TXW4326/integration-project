package aiss.githubminer.service;


import aiss.githubminer.model.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CommitService {

    private final GitHubAPIService gitHubAPIService;

    @Autowired
    public CommitService(GitHubAPIService gitHubAPIService) {
        this.gitHubAPIService = gitHubAPIService;
    }


    public List<Commit> getCommits(String owner, String repo, Integer sinceCommits, Integer maxPages) {
        LocalDateTime now = LocalDateTime.now();
        String resultCommits = now.minusDays(sinceCommits).format(GitHubAPIService.formatter);

        return IntStream.rangeClosed(1, maxPages)
                .parallel()
                .mapToObj(page -> gitHubAPIService.get("repos/{owner}/{repo}/commits?since={sinceCommits}&page={page}", Commit[].class, owner, repo, resultCommits, page))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }
}
