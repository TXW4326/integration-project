package aiss.bitbucketminer.service;

import aiss.bitbucketminer.models.Commit;
import aiss.bitbucketminer.models.Issue;
import aiss.bitbucketminer.models.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BitbucketService {

    @Value("${gitminer.api.url:http://localhost:8080/gitminer/projects}")
    private String gitMinerUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Project fetchData(String workspace, String repoSlug, int nCommits, int nIssues, int maxPages, boolean sendToGitMiner) {
        String baseApi = "https://api.bitbucket.org/2.0/repositories/" + workspace + "/" + repoSlug;

        List<Commit> commitList = new ArrayList<>();
        String commitsUrl = baseApi + "/commits?pagelen=50";
        for (int page = 0; page < maxPages && commitsUrl != null && commitList.size() < nCommits; page++) {
            try {
                var response = restTemplate.getForObject(commitsUrl, BitbucketPaginatedResponse.class);
                if (response == null || response.values() == null) break;
                for (Commit commit : response.values()) {
                    commitList.add(commit);
                    if (commitList.size() >= nCommits) break;
                }
                commitsUrl = response.next();
            } catch (HttpClientErrorException e) {
                break;
            }
        }

        List<Issue> issueList = new ArrayList<>();
        String issuesUrl = baseApi + "/issues?pagelen=50";
        for (int page = 0; page < maxPages && issuesUrl != null && issueList.size() < nIssues; page++) {
            try {
                var response = restTemplate.getForObject(issuesUrl, BitbucketPaginatedResponseIssue.class);
                if (response == null || response.values() == null) break;
                for (Issue issue : response.values()) {
                    issueList.add(issue);
                    if (issueList.size() >= nIssues) break;
                }
                issuesUrl = response.next();
            } catch (HttpClientErrorException e) {
                break;
            }
        }

        Project project = new Project(repoSlug, "http://localhost:8081/bitbucket/" + workspace + "/" + repoSlug);
        project.getCommits().addAll(commitList);
        project.getIssues().addAll(issueList);

        if (sendToGitMiner) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Project> request = new HttpEntity<>(project, headers);
            restTemplate.postForEntity(gitMinerUrl, request, String.class);
        }
        return project;
    }
        private record BitbucketPaginatedResponse(List<Commit> values, String next) {
    }
        private record BitbucketPaginatedResponseIssue(List<Issue> values, String next) {
    }
}