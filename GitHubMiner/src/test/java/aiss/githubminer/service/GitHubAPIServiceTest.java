package aiss.githubminer.service;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.*;
import aiss.githubminer.utils.JsonUtils;
import aiss.githubminer.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GitHubAPIServiceTest {

    private final GitHubAPIService gitHubAPIService;


    @Autowired
    private GitHubAPIServiceTest(GitHubAPIService gitHubAPIService) {
        this.gitHubAPIService = gitHubAPIService;
    }


    @Test
    @DisplayName("get project with valid parameters")
    void getProject() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceCommits = 20;
        int sinceIssues = 10;
        int maxPages = 20;

        Project project = gitHubAPIService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages);
        assertEquals(repo, project.getName(), "Project name should match the repo");
        assertNotNull(project.getId(), "Project id should not be null");
        assertFalse(project.getId().isEmpty(), "Project id should not be empty");
        assertNotNull(project.getWeb_url(), "Project web_url should not be null");
        assertFalse(project.getWeb_url().isEmpty(), "Project web_url should not be empty");
        testCommits(project.getCommits(), maxPages).close();
        testIssues(project.getIssues(), maxPages).forEach(issue->
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
                        gitHubAPIService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
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
                        gitHubAPIService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
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
                        gitHubAPIService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
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
                        gitHubAPIService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
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
                        gitHubAPIService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
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
                        gitHubAPIService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
                "Should throw GitHubMinerException for invalid project"
        );

        TestUtils.assertException(ex, HttpStatus.NOT_FOUND);
        assertEquals("An error occurred while fetching the data for the given parameters", ex.getReason().get("error"),  "Exception message should indicate project not found");
        Map<String,?> parameters = TestUtils.assertParametersInMap(ex.getReason());
        TestUtils.assertMapContains(parameters, "owner", owner);
        TestUtils.assertMapContains(parameters, "repo", repo);
        TestUtils.assertMapContains(parameters, "sinceCommits", sinceCommits);
        TestUtils.assertMapContains(parameters, "sinceIssues", sinceIssues);
        TestUtils.assertMapContains(parameters, "maxPages", maxPages);
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
                        gitHubAPIService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
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
                        gitHubAPIService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages),
                "Should throw GitHubMinerException for null repo"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Repository is null", ex.getMessage(), "Exception message should indicate null repo");
        System.out.println(ex.getMessage());
    }


    private Stream<Commit> testCommits(List<Commit> commits, int maxPages) {
        assertNotNull(commits, "Commits list should not be null");
        assertFalse(commits.isEmpty(), "Commits list should not be empty");
        assertTrue(commits.size() <= maxPages * gitHubAPIService.PER_PAGE, "Commits size should not exceed max pages times PER_PAGE");
        assertEquals(commits.size(), commits.stream().map(Commit::getId).distinct().count(), "Commit ids should be unique");
        return commits.stream().peek(commit -> {
            assertNotNull(commit, "Commit should not be null");
            assertNotNull(commit.getId(), "Commit id should not be null");
            assertFalse(commit.getId().isEmpty(), "Commit id should not be empty");
            assertNotNull(commit.getTitle(), "Commit title should not be null");
            assertNotNull(commit.getWeb_url(), "Commit web URL should not be null");
            assertFalse(commit.getWeb_url().isEmpty(), "Commit web URL should not be empty");
            assertTrue(
                    (commit.getAuthor_name() == null && commit.getAuthor_email() == null && commit.getAuthored_date() == null) ||
                            (commit.getAuthor_name() != null && commit.getAuthor_email() != null && commit.getAuthored_date() != null),
                    "Author fields should be all null or all non-null"
            );
            if (commit.getAuthor_name() != null) {
                assertFalse(commit.getAuthor_name().isEmpty(), "Author name should not be empty");
                assertFalse(commit.getAuthor_email().isEmpty(), "Author email should not be empty");
                assertTrue(commit.getAuthored_date().isBefore(LocalDateTime.now()), "Authored date should be in the past");
            }
            if (commit.getMessage() != null) assertFalse(commit.getMessage().isEmpty(), "Commit message should not be empty");
        });
    }

    private Stream<Issue> testIssues(List<Issue> issues, int maxPages) {
        assertNotNull(issues, "List of issues should not be null");
        assertTrue(issues.size() <= maxPages * gitHubAPIService.PER_PAGE, "Number of issues should not exceed maxPages * PER_PAGE");
        assertEquals(issues.size(), issues.stream().map(Issue::getId).distinct().count(), "Issue IDs should be unique");
        return issues.stream().peek(issue -> {
            assertNotNull(issue, "Issue should not be null");
            assertNotNull(issue.getId(), "Issue id should not be null");
            assertFalse(issue.getId().isEmpty(), "Issue id should not be empty");
            assertNotNull(issue.getTitle(), "Issue title should not be null");
            assertFalse(issue.getTitle().isEmpty(), "Issue title should not be empty");
            assertNotNull(issue.getState(), "Issue state should not be null");
            assertFalse(issue.getState().isEmpty(), "Issue state should not be empty");
            if (issue.getAuthor() != null) testUser(issue.getAuthor());
            assertNotNull(issue.getLabels(), "Issue labels should not be null");
            for (String label : issue.getLabels()) {
                assertNotNull(label, "Issue label should not be null");
                assertFalse(label.isEmpty(), "Issue label should not be empty");
            }
            if (issue.getAssignee() != null) testUser(issue.getAssignee());
            assertTrue(issue.getVotes() >= 0, "Issue votes should be non-negative");
            assertNotNull(issue.getCreated_at(), "Issue created date should not be null");
            assertNotNull(issue.getUpdated_at(), "Issue updated date should not be null");
            assertTrue(issue.getCreated_at().isBefore(LocalDateTime.now()), "Issue created date should be in the past");
            assertFalse(issue.getUpdated_at().isBefore(issue.getCreated_at()), "Issue updated date should not be before created date");
            assertTrue(issue.getUpdated_at().isBefore(LocalDateTime.now()), "Issue updated date should be in the past");
            if (issue.getClosed_at() != null) {
                assertTrue(issue.getClosed_at().isBefore(LocalDateTime.now()), "Issue closed date should be in the past");
                assertTrue(issue.getClosed_at().isAfter(issue.getUpdated_at()), "Issue closed date should be after last updated date");
                assertTrue(issue.getClosed_at().isAfter(issue.getCreated_at()), "Issue closed date should be after created date");
            }
            assertTrue(issue.getDescription() == null || !issue.getDescription().isEmpty(), "Issue description should not be empty");
            testComments(issue.getComments(), maxPages).close();
        });

    }

    private Stream<Comment> testComments(List<Comment> comments, int maxPages) {
        assertNotNull(comments, "Comments should not be null");
        assertTrue(comments.size() <= maxPages * gitHubAPIService.PER_PAGE, "Comments size should not exceed maxPages * 30 (default page size)");
        assertEquals(comments.size(), comments.stream().map(Comment::getId).distinct().count(), "Comment IDs should be unique");

        return comments.stream().peek(comment -> {
            assertNotNull(comment, "Comment should not be null");
            assertNotNull(comment.getId(), "Comment ID should not be null");
            assertFalse(comment.getId().isEmpty(), "Comment ID should not be empty");
            assertNotNull(comment.getBody(), "Comment body should not be null");
            assertFalse(comment.getBody().isEmpty(), "Comment body should not be empty");
            if (comment.getAuthor() != null) testUser(comment.getAuthor());
            assertNotNull(comment.getCreated_at(), "Comment creation date should not be null");
            assertNotNull(comment.getUpdated_at(), "Comment update date should not be null");
            assertFalse(comment.getCreated_at().isAfter(comment.getUpdated_at()), "Comment creation date should not be after update date");
            assertTrue(comment.getCreated_at().isBefore(LocalDateTime.now()), "Comment creation date should be in the past");
            assertTrue(comment.getUpdated_at().isBefore(LocalDateTime.now()), "Comment update date should be in the past");
        });
    }

    public void testUser(User user) {
        assertNotNull(user, "User should not be null");
        assertNotNull(user.getUsername(), "User login should not be null");
        assertFalse(user.getUsername().isEmpty(), "User login should not be empty");
        assertNotNull(user.getId(), "User id should not be null");
        assertFalse(user.getId().isEmpty(), "User id should not be empty");
        assertNotNull(user.getAvatar_url(), "User avatar URL should not be null");
        assertFalse(user.getAvatar_url().isEmpty(), "User avatar URL should not be empty");
        assertNotNull(user.getWeb_url(), "User web URL should not be null");
        assertFalse(user.getWeb_url().isEmpty(), "User web URL should not be empty");
        assertTrue(user.getName() == null || !user.getName().isEmpty(), "User name should not be empty");
    }
}