package aiss.githubminer.service;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Commit;
import aiss.githubminer.utils.JsonUtils;
import aiss.githubminer.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommitServiceTest {


    private final CommitService commitService;

    @Autowired
    public CommitServiceTest(CommitService commitService) {
        this.commitService = commitService;
    }


    @Test
    @DisplayName("Get commits with valid parameters")
    void getCommits() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceCommits = 2;
        int maxPages = 2;

        List<Commit> commits = commitService.getCommits(owner, repo, sinceCommits, maxPages);
        testCommits(commits, maxPages).close();
        System.out.println(JsonUtils.toJson(commits));
    }

    @Test
    @DisplayName("Get commits with invalid project")
    void getCommitsWithInvalidRepo() {
        String owner = "invalid-ownerytfvcdtvgybtfrtvgybvf";
        String repo = "invalid-repoyvfgbhubgyvfhbubgyvfb";
        int sinceCommits = 2;
        int maxPages = 1;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
                commitService.getCommits(owner, repo, sinceCommits, maxPages),
                "Should throw GitHubMinerException for invalid owner or repo"
        );
        TestUtils.assertException(ex, HttpStatus.NOT_FOUND);
        assertEquals("No commits found for the given parameters", ex.getReason().get("error"), "Error message should match expected");
        Map<?, ?> parameters = TestUtils.assertParametersInMap(ex.getReason());
        TestUtils.assertMapContains(parameters, "owner", owner);
        TestUtils.assertMapContains(parameters, "repo", repo);
        TestUtils.assertMapContains(parameters, "sinceCommits", sinceCommits);
        TestUtils.assertMapContains(parameters, "maxPages", maxPages);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get commits with invalid sinceCommits")
    void getCommitsWithInvalidSinceCommits() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceCommits = -1; // Invalid sinceCommits
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
                commitService.getCommits(owner, repo, sinceCommits, maxPages),
                "Should throw GitHubMinerException for invalid sinceCommits"
        );
        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("sinceCommits value cannot be negative: " + sinceCommits, ex.getReason().get("error"), "Error message should match expected");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get commits with invalid maxPages")
    void getCommitsWithInvalidMaxPages() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceCommits = 2;
        int maxPages = -1; // Invalid maxPages

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
                commitService.getCommits(owner, repo, sinceCommits, maxPages),
                "Should throw GitHubMinerException for invalid maxPages"
        );
        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("maxPages value cannot be negative: " + maxPages, ex.getReason().get("error"), "Error message should match expected");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get commits with empty repo")
    void getCommitsWithEmptyRepo() {
        String owner = "spring-projects";
        String repo = ""; // Empty repo
        int sinceCommits = 2;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
                commitService.getCommits(owner, repo, sinceCommits, maxPages),
                "Should throw GitHubMinerException for empty repo"
        );
        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Repository is empty", ex.getReason().get("error"), "Error message should match expected");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get commits with null owner")
    void getCommitsWithNullOwner() {
        String owner = null; // Null owner
        String repo = "spring-framework";
        int sinceCommits = 2;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
                commitService.getCommits(owner, repo, sinceCommits, maxPages),
                "Should throw GitHubMinerException for null owner"
        );
        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Owner is null", ex.getReason().get("error"), "Error message should match expected");
        System.out.println(ex.getMessage());
    }


    @Test
    @DisplayName("Get commits with empty owner")
    void getCommitsWithEmptyOwner() {
        String owner = ""; // Empty owner
        String repo = "spring-framework";
        int sinceCommits = 2;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
                commitService.getCommits(owner, repo, sinceCommits, maxPages),
                "Should throw GitHubMinerException for empty owner"
        );
        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Owner is empty", ex.getReason().get("error"), "Error message should match expected");
        System.out.println(ex.getMessage());
    }


    public static Stream<Commit> testCommits(List<Commit> commits, int maxPages) {
        assertNotNull(commits, "Commits list should not be null");
        assertFalse(commits.isEmpty(), "Commits list should not be empty");
        assertTrue(commits.size() <= maxPages * 30, "Commits size should not exceed max pages times 30 (commits per page)");
        assertEquals(commits.size(), commits.stream().map(Commit::getId).distinct().count(), "Commits list should be equal to commits size");
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
        });
    }
}