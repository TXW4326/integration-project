package aiss.githubminer.service;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Issue;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IssueServiceTest {

    private final IssueService issueService;
    private final int PER_PAGE;

    @Autowired
    public IssueServiceTest(IssueService issueService,
                            @Value("${github.default.perPage:}") int perPage) {
        this.issueService = issueService;
        this.PER_PAGE = perPage;
    }

    @Test
    @DisplayName("Get issues with valid parameters")
    void getIssues() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceIssues = 20;
        int maxPages = 2;

        List<Issue> issues = issueService.getIssues(owner, repo, sinceIssues, maxPages);
        testIssues(issues, maxPages, PER_PAGE)
            .forEach(issue ->
                    assertTrue(ChronoUnit.DAYS.between(issue.getUpdated_at(), LocalDateTime.now()) <= sinceIssues,
                            "Issue updated date should be within the sinceIssues range")
            );
        System.out.println(JsonUtils.toJson(issues));
    }

    @Test
    @DisplayName("Get issues with empty owner")
    void getIssuesWithEmptyOwner() {
        String owner = "";
        String repo = "spring-framework";
        int sinceIssues = 20;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class,
                () -> issueService.getIssues(owner, repo, sinceIssues, maxPages),
                "Should throw GitHubMinerException for empty owner"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Owner is empty", ex.getMessage(), "Error message should match expected");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get issues with empty repo")
    void getIssuesWithEmptyRepo() {
        String owner = "spring-projects";
        String repo = "";
        int sinceIssues = 20;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class,
                () -> issueService.getIssues(owner, repo, sinceIssues, maxPages),
                "Should throw GitHubMinerException for empty repo"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Repository is empty", ex.getMessage(), "Error message should match expected");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get issues with invalid sinceIssues")
    void getIssuesWithInvalidSinceIssues() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceIssues = -1; // Invalid sinceIssues
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class,
                () -> issueService.getIssues(owner, repo, sinceIssues, maxPages),
                "Should throw GitHubMinerException for invalid sinceIssues"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("sinceIssues value cannot be negative: " + sinceIssues, ex.getMessage(), "Error message should match expected");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get issues with invalid maxPages")
    void getIssuesWithInvalidMaxPages() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceIssues = 20;
        int maxPages = -1; // Invalid maxPages

        GitHubMinerException ex = assertThrows(GitHubMinerException.class,
                () -> issueService.getIssues(owner, repo, sinceIssues, maxPages),
                "Should throw GitHubMinerException for invalid maxPages"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("maxPages value cannot be negative: " + maxPages, ex.getMessage(), "Error message should match expected");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get issues with invalid project")
    void getIssuesWithNonExistingOwnerAndRepo() {
        String owner = "invalid-ownertrfvghbuy7gtvyhu7gt6fvgy";
        String repo = "invalid-repoyvftghbyutfredsw34d55f678y";
        int sinceIssues = 20;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class,
                () -> issueService.getIssues(owner, repo, sinceIssues, maxPages),
                "Should throw GitHubMinerException for non-existing owner and repo"
        );

        TestUtils.assertException(ex, HttpStatus.NOT_FOUND);
        assertEquals("No issues found for the given parameters", ex.getReason().get("error"), "Error message should match expected");
        LinkedHashMap<String,?> parameters = TestUtils.assertParametersInMap(ex.getReason());
        TestUtils.assertMapContains(parameters, "sinceIssues", sinceIssues);
        TestUtils.assertMapContains(parameters, "maxPages", maxPages);
        TestUtils.assertMapContains(parameters, "owner", owner);
        TestUtils.assertMapContains(parameters, "repo", repo);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get issues with null owner")
    void getIssuesWithNullOwner() {
        String owner = null;
        String repo = "spring-framework";
        int sinceIssues = 20;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class,
                () -> issueService.getIssues(owner, repo, sinceIssues, maxPages),
                "Should throw GitHubMinerException for null owner"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Owner is null", ex.getMessage(), "Error message should match expected");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get issues with null repo")
    void getIssuesWithNullRepo() {
        String owner = "spring-projects";
        String repo = null;
        int sinceIssues = 20;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class,
                () -> issueService.getIssues(owner, repo, sinceIssues, maxPages),
                "Should throw GitHubMinerException for null repo"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Repository is null", ex.getMessage(), "Error message should match expected");
        System.out.println(ex.getMessage());
    }

    public static Stream<Issue> testIssues(List<Issue> issues, int maxPages, int PER_PAGE) {
        assertNotNull(issues, "List of issues should not be null");
        assertTrue(issues.size() <= maxPages * PER_PAGE, "Number of issues should not exceed maxPages * 30 (30 issues per page)");
        assertEquals(issues.size(), issues.stream().map(Issue::getId).distinct().count(), "Issue IDs should be unique");

        return issues.stream().peek(issue -> {
            assertTrue(issue.getId() >= 0, "Issue ID should be non-negative");
            assertNotNull(issue.getTitle(), "Issue title should not be null");
            assertFalse(issue.getTitle().isEmpty(), "Issue title should not be empty");
            assertNotNull(issue.getState(), "Issue state should not be null");
            assertFalse(issue.getState().isEmpty(), "Issue state should not be empty");
            if (issue.getAuthor() != null) UserServiceTest.testUser(issue.getAuthor());
            assertNotNull(issue.getLabels(), "Issue labels should not be null");
            for (String label : issue.getLabels()) {
                assertNotNull(label, "Issue label should not be null");
                assertFalse(label.isEmpty(), "Issue label should not be empty");
            }
            if (issue.getAssignee() != null) UserServiceTest.testUser(issue.getAssignee());
            assertTrue(issue.getNumber() >= 0, "Issue number should be non-negative");
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
            assertEquals("open", issue.getState(), "Issue state should be 'open'");
            assertTrue(issue.getDescription() == null || !issue.getDescription().isEmpty(), "Issue description should not be empty");
            CommentServiceTest.testComments(issue.getComments(), maxPages, PER_PAGE).close();
        });

    }

}