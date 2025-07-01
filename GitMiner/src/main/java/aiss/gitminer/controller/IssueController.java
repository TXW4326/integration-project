package aiss.gitminer.controller;

import aiss.gitminer.dto.ErrorResponse;
import aiss.gitminer.exception.BadRequestException;
import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repositories.IssueRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(
        name = "Issue",
        description = "Issue public API"
)
@ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
@RestController
@RequestMapping("/gitminer/issues")
public class IssueController {

    private final IssueRepository issueRepository;

    @Autowired
    private IssueController(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    // API Documentation:
    // GET http://localhost:8080/gitminer/issues
    @Operation(
            summary = "Get a list of issues",
            description = "Get a list of issues",
            tags = {"GET", "getAll", "Issues", "Issue"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Issue.class)), mediaType = "application/json"))
    })

    // API Route and method:
    @GetMapping
    public List<Issue> getIssues() {
        List<Issue> issues = issueRepository.findAll();
        //TODO: Why not return an empty list instead of throwing an exception?
        if (issues.isEmpty()) throw new IssueNotFoundException();
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
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json"))
    })

    // API Route and method:
    @GetMapping("/{id}")
    public Issue getIssueById(@Parameter(description = "Searched issue ID")@PathVariable String id) {
        if (id.isBlank()) throw new BadRequestException("Issue ID cannot be blank");
        Optional<Issue> issue = issueRepository.findById(id);
        if (issue.isEmpty()) throw new IssueNotFoundException();
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
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Comment.class)), mediaType = "application/json"))
    })

    // API Route and method:
    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsByIssueId(@Parameter(description = "Id of the issue whose comments are being requested") @PathVariable String id) {
        if (id.isBlank()) throw new BadRequestException("Issue ID cannot be blank");
        Optional<Issue> issue = issueRepository.findById(id);
        if (issue.isEmpty()) throw new IssueNotFoundException();
        //TODO: Here you return an empty list if the issue has no comments, but everywhere else you always check if the returned list is empty.
        return issue.get().getComments();
    }

}