package aiss.githubminer.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GitHubAPIServiceTest {

    private GitHubAPIService gitHubAPIService;


    @Autowired
    private GitHubAPIServiceTest(GitHubAPIService gitHubAPIService) {
        this.gitHubAPIService = gitHubAPIService;
    }


    @Test
    @DisplayName("Test get project with valid parameters")
    void getProject() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceCommits = 5;
        int sinceIssues = 10;
        int maxPages = 4;

        gitHubAPIService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages);
    }
}