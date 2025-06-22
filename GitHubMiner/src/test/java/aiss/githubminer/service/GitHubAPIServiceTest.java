package aiss.githubminer.service;

import aiss.githubminer.model.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GitHubAPIServiceTest {

    private final GitHubAPIService gitHubAPIService;


    @Autowired
    private GitHubAPIServiceTest(GitHubAPIService gitHubAPIService) {
        this.gitHubAPIService = gitHubAPIService;
    }


    @Test
    @DisplayName("Test get project with valid parameters")
    void getProject() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceCommits = 50;
        int sinceIssues = 50;
        int maxPages = 10;
        //TODO: Explicar a la profesora problema de los pull requests entre los issues de la api rest
        //TODO: Testear obtención de comentarios con queries adicionales
        Project project = gitHubAPIService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages);
        System.out.println(project.getCommits().size());
        System.out.println(project.getIssues().size());
    }
}