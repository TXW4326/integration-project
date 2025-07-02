package aiss.gitminer.controller;

import aiss.gitminer.dto.ErrorResponse;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.services.CommentService;
import aiss.gitminer.services.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Issue",
        description = "Issue public API"
)
@ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
@RestController
@RequestMapping("/gitminer/issues")
public class IssueController {

    private final IssueService issueService;
    private final CommentService commentService;

    @Autowired
    private IssueController(
            IssueService issueService,
            CommentService commentService
        ) {
        this.issueService = issueService;
        this.commentService = commentService;
    }

    // API Documentation:
    // GET http://localhost:8080/gitminer/issues
    @Operation(
            summary = "Get a list of issues",
            description = "Get a list of issues",
            tags = {"GET", "getAll", "Issues", "Issue"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Issue.class)), mediaType = "application/json")),
    })

    // API Route and method:
    @GetMapping
    public List<Issue> getIssues(
            @Parameter(description = "Issue page number, default is 0") @RequestParam(defaultValue = "0") Integer issuePage,
            @Parameter(description = "Issue page size, default is 30") @RequestParam(defaultValue = "30") Integer issuePageSize,
            @Parameter(description = "Comment page number, default is 0") @RequestParam(defaultValue = "0") Integer commentPage,
            @Parameter(description = "Comment page size, default is 30") @RequestParam(defaultValue = "30") Integer commentPageSize,
            @Parameter(description = "Issue author ID") @RequestParam(required = false) String authorId,
            @Parameter(description = "Issue state") @RequestParam(required = false) String state
    ) {
        Pageable pageIssue = PageRequest.of(issuePage, issuePageSize);
        Pageable pageComment = PageRequest.of(commentPage, commentPageSize);
        if (authorId != null && state != null) {
            return issueService.findAllByAuthorIdAndState(authorId, state, pageIssue, pageComment);
        } else if (authorId != null) {
            return issueService.findAllByAuthorId(authorId, pageIssue, pageComment);
        } else if (state != null) {
            return issueService.findAllByState(state, pageIssue, pageComment);
        } else {
            return issueService.findAll(pageIssue, pageComment);
        }
    }

    // API Documentation:
    // Get http://localhost:8080/gitminer/issues/{id}
    @Operation(
            summary = "Get issue by ID",
            description = "Get issue by ID",
            tags = {"GET", "getById", "Issue"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")),
    })

    // API Route and method:
    @GetMapping("/{id}")
    public Issue getIssueById(
            @Parameter(description = "Searched issue ID")@PathVariable String id,
            @Parameter(description = "Comment page number, default is 0") @RequestParam(defaultValue = "0") Integer commentPage,
            @Parameter(description = "Comment page size, default is 30") @RequestParam(defaultValue = "30") Integer commentPageSize
    ) {
        Pageable pageComment = PageRequest.of(commentPage, commentPageSize);
        return issueService.findIssueById(id,pageComment);
    }

    // API Documentation:
    // GET http://localhost:8080/gitminer/issues/{id}/comments
    @Operation(
            summary = "Get a list of comments given an issue",
            description = "Get a list of comments given an issue",
            tags = {"GET", "getById", "Issue", "Comments", "Comment"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Comment.class)), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")),
    })

    // API Route and method:
    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsByIssueId(
            @Parameter(description = "Id of the issue whose comments are being requested") @PathVariable String id,
            @Parameter(description = "Comment page number, default is 0") @RequestParam(defaultValue = "0") Integer commentPage,
            @Parameter(description = "Comment page size, default is 30") @RequestParam(defaultValue = "30") Integer commentPageSize
    ) {
        Pageable pageComment = PageRequest.of(commentPage, commentPageSize);
        return commentService.findByIssueId(id,pageComment);
    }

}