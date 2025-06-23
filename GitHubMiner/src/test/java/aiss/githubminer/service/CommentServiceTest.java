package aiss.githubminer.service;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Comment;
import aiss.githubminer.utils.JsonUtils;
import aiss.githubminer.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    private final CommentService commentService;
    private final int PER_PAGE;

    @Autowired
    public CommentServiceTest(CommentService commentService,
                              @Value("${github.default.perPage:}") int perPage) {
        this.commentService = commentService;
        this.PER_PAGE = perPage;
    }


    @Test
    @DisplayName("Get comments with valid parameters")
    void getComments() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int issueNumber = 35042;
        int maxPages = 2;

        List<Comment> comments = commentService.getComments(owner, repo, issueNumber, maxPages);
        testComments(comments, maxPages, PER_PAGE).close();
        System.out.println(JsonUtils.toJson(comments));
    }

    @Test
    @DisplayName("Get comments with invalid project")
    void getCommentsInvalidProject() {
        String owner = "invalid-ownervtfgybtfr5tvg76fr5cdtfvgy7hgt6y";
        String repo = "invalid-repohuygbuh87gt6frvygt6fr5cdtfv6gr5cdtfvygbuv";
        int issueNumber = 35042;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            commentService.getComments(owner, repo, issueNumber, maxPages),
            "Should throw GitHubMinerException for invalid project"
        );

        TestUtils.assertException(ex, HttpStatus.NOT_FOUND);
        assertEquals("Comments not found", ex.getReason().get("error"), "Exception message should indicate no issues found");
        Map<String,?> parameters = TestUtils.assertParametersInMap(ex.getReason());
        TestUtils.assertMapContains(parameters, "owner", owner);
        TestUtils.assertMapContains(parameters, "repo", repo);
        TestUtils.assertMapContains(parameters, "issueNumber", issueNumber);
        TestUtils.assertMapContains(parameters, "maxPages", maxPages);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get comments with empty repo")
    void getCommentsEmptyRepo() {
        String owner = "spring-projects";
        String repo = "";
        int issueNumber = 35042;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            commentService.getComments(owner, repo, issueNumber, maxPages),
            "Should throw GitHubMinerException for empty repo"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Repository is empty", ex.getMessage(), "Exception message should indicate empty repo");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get comments empty owner")
    void getCommentsEmptyOwner() {
        String owner = "";
        String repo = "spring-framework";
        int issueNumber = 35042;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            commentService.getComments(owner, repo, issueNumber, maxPages),
            "Should throw GitHubMinerException for empty owner"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Owner is empty", ex.getMessage(), "Exception message should indicate empty owner");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get comments with null owner")
    void getCommentsNullOwner() {
        String owner = null;
        String repo = "spring-framework";
        int issueNumber = 35042;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            commentService.getComments(owner, repo, issueNumber, maxPages),
            "Should throw GitHubMinerException for null owner"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Owner is null", ex.getMessage(), "Exception message should indicate null owner");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get comments with null repo")
    void getCommentsNullRepo() {
        String owner = "spring-projects";
        String repo = null;
        int issueNumber = 35042;
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            commentService.getComments(owner, repo, issueNumber, maxPages),
            "Should throw GitHubMinerException for null repo"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Repository is null", ex.getMessage(), "Exception message should indicate null repo");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get comments with non-existent issue")
    void getCommentsNonExistentIssue() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int issueNumber = 99999999; // Assuming this issue does not exist
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            commentService.getComments(owner, repo, issueNumber, maxPages),
            "Should throw GitHubMinerException for non-existent issue"
        );

        TestUtils.assertException(ex, HttpStatus.NOT_FOUND);
        assertEquals("Comments not found", ex.getReason().get("error"), "Exception message should indicate comment not found");
        Map<String,?> parameters = TestUtils.assertParametersInMap(ex.getReason());
        TestUtils.assertMapContains(parameters, "owner", owner);
        TestUtils.assertMapContains(parameters, "repo", repo);
        TestUtils.assertMapContains(parameters, "issueNumber", issueNumber);
        TestUtils.assertMapContains(parameters, "maxPages", maxPages);
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get comments with invalid maxPages")
    void getCommentsInvalidMaxPages() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int issueNumber = 35042;
        int maxPages = -1; // Invalid maxPages

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
            commentService.getComments(owner, repo, issueNumber, maxPages),
            "Should throw GitHubMinerException for invalid maxPages"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("maxPages value cannot be negative: " + maxPages, ex.getMessage(), "Exception message should indicate invalid maxPages");
        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get comments with invalid issueNumber")
    void getCommentsInvalidIssueNumber() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int issueNumber = -1; // Invalid issue number
        int maxPages = 2;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () ->
                commentService.getComments(owner, repo, issueNumber, maxPages),
                "Should throw GitHubMinerException for invalid issue number"
        );

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Issue number cannot be negative:  " + issueNumber, ex.getMessage(), "Exception message should indicate invalid issue number");
        System.out.println(ex.getMessage());
    }

    public static Stream<Comment> testComments(List<Comment> comments, int maxPages, int PER_PAGE) {
        assertNotNull(comments, "Comments should not be null");
        assertTrue(comments.size() <= maxPages * PER_PAGE, "Comments size should not exceed maxPages * PER_PAGE");
        assertEquals(comments.size(), comments.stream().map(Comment::getId).distinct().count(), "Comment IDs should be unique");

       return comments.stream().peek(comment -> {
            assertNotNull(comment, "Comment should not be null");
            assertNotNull(comment.getBody(), "Comment body should not be null");
            assertFalse(comment.getBody().isEmpty(), "Comment body should not be empty");
            if (comment.getAuthor() != null) UserServiceTest.testUser(comment.getAuthor());
            assertNotNull(comment.getCreated_at(), "Comment creation date should not be null");
            assertNotNull(comment.getUpdated_at(), "Comment update date should not be null");
            assertTrue(comment.getId() >= 0, "Comment ID should be non-negative");
            assertFalse(comment.getCreated_at().isAfter(comment.getUpdated_at()), "Comment creation date should not be after update date");
            assertTrue(comment.getCreated_at().isBefore(LocalDateTime.now()), "Comment creation date should be in the past");
            assertTrue(comment.getUpdated_at().isBefore(LocalDateTime.now()), "Comment update date should be in the past");
        });
    }
}