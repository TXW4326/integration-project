package aiss.githubminer.service;

import aiss.githubminer.model.Commit;
import aiss.githubminer.model.Issue;
import aiss.githubminer.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    GitHubAPIService gitHubAPIService;

    public Project getProject(String owner, String repo, Integer sinceCommits, Integer sinceIssues, Integer maxPagess) {
        String url = "repos/" + owner + "/" + repo;
        Project project = gitHubAPIService.get(url, Project.class);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String resultIssues = now.minusDays(sinceIssues).format(formatter);
        String resultCommits = now.minusDays(sinceCommits).format(formatter);
        Commit[] commits = gitHubAPIService.get("repos/" + owner + "/" + repo + "/commits?since=" + resultCommits + "&per_page=" + maxPagess, Commit[].class);
        //Issue[] issues = gitHubAPIService.get("repos/" + owner + "/" + repo + "/issues?since=" + resultIssues + "&per_page=" + maxPagess, Issue[].class);
        project.setCommits(Arrays.stream(commits).collect(Collectors.toList()));
        return project;
    }

}
