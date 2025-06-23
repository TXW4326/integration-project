package aiss.githubminer.service;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Project;
import aiss.githubminer.utils.JsonUtils;
import aiss.githubminer.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServiceTest {


    private final ProjectService projectService;
    private final int PER_PAGE;

    @Autowired
    public ProjectServiceTest (ProjectService projectService,
                               @Value("${github.default.perPage:}") int perPage) {
        this.projectService = projectService;
        this.PER_PAGE = perPage;
    }


    @Test
    @DisplayName("Get project with valid parameters")
    void getProject() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceCommits = 2;
        int sinceIssues = 20;
        int maxPages = 2;

        Project project = projectService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages);

        assertNotNull(project, "Project should not be null");
        assertEquals("spring-framework", project.getName(), "Project name should be 'spring-framework'");
        assertEquals("https://github.com/spring-projects/spring-framework", project.getWeb_url(), "Project web URL should match");
        assertEquals(1148753, project.getId(), "Project ID should match");
        CommitServiceTest.testCommits(project.getCommits(), maxPages, PER_PAGE).close();
        IssueServiceTest.testIssues(project.getIssues(), maxPages, PER_PAGE).forEach(issue ->
                assertTrue(ChronoUnit.DAYS.between(issue.getUpdated_at(), LocalDateTime.now()) <= sinceIssues,
                        "Issue updated date should be within the sinceIssues range")
        );
        System.out.println(JsonUtils.toJson(project));
    }

    @Test
    @DisplayName("Get project with empty owner")
    void getProjectWithEmptyOwner() {
        String owner = "";
        String repo = "spring-framework";
        int sinceCommits = 2;
        int sinceIssues = 20;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            projectService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
            "Should throw GitHubMinerException for empty owner"
        );
        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals( "Owner is empty", ex.getMessage(), "Exception message should indicate empty owner");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get project with empty repo")
    void getProjectWithEmptyRepo() {
        String owner = "spring-projects";
        String repo = "";
        int sinceCommits = 2;
        int sinceIssues = 20;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            projectService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
            "Should throw GitHubMinerException for empty repo"
        );
        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Repository is empty", ex.getMessage(), "Exception message should indicate empty repo");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get project with invalid sinceCommits")
    void getProjectWithInvalidSinceCommits() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceCommits = -1; // Invalid value
        int sinceIssues = 20;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            projectService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
            "Should throw GitHubMinerException for invalid sinceCommits"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("sinceCommits value cannot be negative: " + sinceCommits, ex.getMessage(), "Exception message should indicate invalid sinceCommits");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get project with invalid sinceIssues")
    void getProjectWithInvalidSinceIssues() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceCommits = 2;
        int sinceIssues = -1; // Invalid value
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            projectService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
            "Should throw GitHubMinerException for invalid sinceIssues"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("sinceIssues value cannot be negative: " + sinceIssues, ex.getMessage(), "Exception message should indicate invalid sinceIssues");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get project with invalid maxPages")
    void getProjectWithInvalidMaxPages() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceCommits = 2;
        int sinceIssues = 20;
        int maxPages = -1; // Invalid value

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            projectService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
            "Should throw GitHubMinerException for invalid maxPages"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("maxPages value cannot be negative: " + maxPages, ex.getMessage(), "Exception message should indicate invalid maxPages");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get project with invalid project")
    void getProjectWithInvalidProject() {
        String owner = "invalid-ownerytvfgybh7gt6frgyhu7gt6frbyuh8bghni";
        String repo = "invalid-repojuhygvbuhnygtvybuh8yvgu7hgt6frv5ygbt6frv5t";
        int sinceCommits = 2;
        int sinceIssues = 20;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            projectService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
            "Should throw GitHubMinerException for invalid project"
        );

        TestUtils.assertException(ex, HttpStatus.NOT_FOUND);
        assertEquals("Project not found: " + owner + "/" + repo, ex.getMessage(), "Exception message should indicate project not found");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get project with null owner")
    void getProjectWithNullOwner() {
        String owner = null;
        String repo = "spring-framework";
        int sinceCommits = 2;
        int sinceIssues = 20;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            projectService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
            "Should throw GitHubMinerException for null owner"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Owner is null", ex.getMessage(), "Exception message should indicate null owner");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get project with null repo")
    void getProjectWithNullRepo() {
        String owner = "spring-projects";
        String repo = null;
        int sinceCommits = 2;
        int sinceIssues = 20;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
                projectService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
                "Should throw GitHubMinerException for null repo"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Repository is null", ex.getMessage(), "Exception message should indicate null repo");
        System.out.println(ex.getMessage());
    }

}