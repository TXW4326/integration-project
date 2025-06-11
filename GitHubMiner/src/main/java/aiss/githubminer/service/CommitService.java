package aiss.githubminer.service;


import aiss.githubminer.model.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.List;

@Service
public class CommitService {

    @Autowired
    GitHubAPIService gitHubAPIService;


    public List<Commit> getCommits(String owner, String repo, Integer sinceCommits, Integer maxPages) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String resultCommits = now.minusDays(sinceCommits).format(GitHubAPIService.formatter);

        return IntStream.rangeClosed(1, maxPages)
                .mapToObj(page -> gitHubAPIService.get("repos/{owner}/{repo}/commits?since={sinceCommits}&page={page}", Commit[].class, owner, repo, resultCommits, page))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }
}
