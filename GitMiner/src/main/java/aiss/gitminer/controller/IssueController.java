package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repositories.IssueRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(
        name = "Issue",
        description = "Issue public API"
)
@RestController
public class IssueController {

    @Autowired
    IssueRepository issueRepository;

    // API Documentation:
    // GET http://localhost:8080/gitminer/issues
    @Operation(
            summary = "Get a list of issues",
            description = "Get a list of issues",
            tags = {"GET", "getAll", "Issues", "Issue"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content (schema = @Schema())})
    })

    // API Route and method:
    @GetMapping("/issues")
    public List<Issue> getIssues() {
        List<Issue> issues = issueRepository.findAll();
        if (issues.isEmpty()) {
            throw new IssueNotFoundException("No issues found");
        }
        return issues;
    }

    // API Documentation:
    // Get http://localhost:8080/gitminer/issues/{id}
    @Operation(
            summary = "Get issue by ID",
            description = "Get issue by ID",
            tags = {"GET", "getById", "Issue"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content (schema = @Schema())})

    })

    // API Route and method:
    @GetMapping("/issues/{id}")
    public Issue getIssueById(@Parameter(description = "Searched issue ID")@PathVariable String id) {
        Optional<Issue> issue = issueRepository.findById(id);
        if (!issue.isPresent()) {
            throw new IssueNotFoundException("Issue not found");
        }
        return issue.get();
    }

    // API Documentation:
    // GET http://localhost:8080/gitminer/issues/{id}/comments
    @Operation(
            summary = "Get a list of comments given an issue",
            description = "Get a list of comments given an issue",
            tags = {"GET", "getById", "Issue", "Comments", "Comment"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content (schema = @Schema())})
    })

    // API Route and method:
    @GetMapping("/issues/{id}/comments")
    public List<Comment> getComentsByIssueId(@Parameter(description = "Id of the issue whose comments are being requested") @PathVariable String id) {
        Optional<Issue> issue = issueRepository.findById(id);
        if (!issue.isPresent()) {
            throw new IssueNotFoundException("Issue not found");
        }
        return issue.get().getComments();
    }

}