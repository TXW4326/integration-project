package aiss.githubminer.service;

import aiss.githubminer.model.Commit;
import aiss.githubminer.utils.JsonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import aiss.githubminer.exception.GitHubMinerException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
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
    @DisplayName("Test getCommits with valid parameters")
    void getCommits() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceCommits = 2;
        int maxPages = 2;

        List<Commit> commits = commitService.getCommits(owner, repo, sinceCommits, maxPages);
        testCommits(commits, maxPages).close();
        System.out.println(JsonUtils.toJson(commits));
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