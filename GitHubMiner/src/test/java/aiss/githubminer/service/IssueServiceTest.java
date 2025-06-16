package aiss.githubminer.service;

import aiss.githubminer.model.Issue;
import aiss.githubminer.utils.JsonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IssueServiceTest {

    private final IssueService issueService;

    @Autowired
    public IssueServiceTest(IssueService issueService) {
        this.issueService = issueService;
    }


    @Test
    @DisplayName("Test to get issues from a repository")
    void getIssues() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int sinceIssues = 20;
        int maxPages = 2;

        List<Issue> issues = issueService.getIssues(owner, repo, sinceIssues, maxPages);
        testIssues(issues, maxPages)
            .forEach(issue ->
                    assertTrue(ChronoUnit.DAYS.between(issue.getUpdated_at(), LocalDateTime.now()) <= sinceIssues,
                            "Issue updated date should be within the sinceIssues range")
            );
        System.out.println(JsonUtils.toJson(issues));
    }

    public static Stream<Issue> testIssues(List<Issue> issues, int maxPages) {
        assertNotNull(issues, "List of issues should not be null");
        assertTrue(issues.size() <= maxPages * 30, "Number of issues should not exceed maxPages * 30 (30 issues per page)");
        return issues.stream().peek(issue -> {
            assertTrue(issue.getId() >= 0, "Issue ID should be non-negative");
            assertNotNull(issue.getTitle(), "Issue title should not be null");
            assertFalse(issue.getTitle().isEmpty(), "Issue title should not be empty");
            assertNotNull(issue.getWeb_url(), "Issue web URL should not be null");
            assertFalse(issue.getWeb_url().isEmpty(), "Issue web URL should not be empty");
            assertNotNull(issue.getState(), "Issue state should not be null");
            assertFalse(issue.getState().isEmpty(), "Issue state should not be empty");
            if (issue.getDescription() != null) assertFalse(issue.getDescription().isEmpty(), "Issue description should not be empty");
            if (issue.getAuthor() != null) UserServiceTest.testUser(issue.getAuthor());
            assertNotNull(issue.getLabels(), "Issue labels should not be null");
            for (String label : issue.getLabels()) {
                assertNotNull(label, "Issue label should not be null");
                assertFalse(label.isEmpty(), "Issue label should not be empty");
            }
            if (issue.getAssignee() != null) UserServiceTest.testUser(issue.getAssignee());
            assertNotNull(issue.getNumber(), "Issue number should not be null");
            assertTrue(issue.getNumber() >= 0, "Issue number should be non-negative");
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
            if (issue.getDescription() != null) assertFalse(issue.getDescription().isEmpty(), "Issue description should not be empty");
            CommentServiceTest.testComments(issue.getComments(), maxPages).close();
        });

    }

}