package aiss.githubminer.service;

import aiss.githubminer.model.Project;
import aiss.githubminer.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServiceTest {


    private final ProjectService projectService;

    @Autowired
    public ProjectServiceTest (ProjectService projectService) {
        this.projectService = projectService;
    }


    @Test
    @DisplayName("Get project details for spring-projects/spring-framework")
    void getProject() throws JsonProcessingException {
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
        CommitServiceTest.testCommits(project.getCommits(), maxPages).close();
        assertTrue(project.getCommits().size() <= maxPages * 30, "Commits should not exceed max pages * 30 (page size)");
        IssueServiceTest.testIssues(project.getIssues(), maxPages).forEach(issue ->
                assertTrue(ChronoUnit.DAYS.between(issue.getUpdated_at(), LocalDateTime.now()) <= sinceIssues,
                        "Issue updated date should be within the sinceIssues range")
        );
        assertTrue(project.getIssues().size() <= maxPages * 30, "Issues should not exceed max pages * 30 (page size)");

        System.out.println(JsonUtils.toJson(project));
    }
}