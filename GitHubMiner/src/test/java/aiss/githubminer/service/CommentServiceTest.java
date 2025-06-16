package aiss.githubminer.service;

import aiss.githubminer.model.Comment;
import aiss.githubminer.utils.JsonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    private final CommentService commentService;


    @Autowired
    public CommentServiceTest(CommentService commentService) {
        this.commentService = commentService;
    }


    @Test
    @DisplayName("Test getComments with valid parameters")
    void getComments() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int issueNumber = 35042;
        int maxPages = 2;

        List<Comment> comments = commentService.getComments(owner, repo, issueNumber, maxPages);
        testComments(comments, maxPages).close();
        System.out.println(JsonUtils.toJson(comments));
    }

    public static Stream<Comment> testComments(List<Comment> comments, int maxPages) {
        assertNotNull(comments, "Comments should not be null");
        assertTrue(comments.size() <= maxPages * 30, "Comments size should not exceed maxPages * 30 (default page size)");
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