package aiss.gitminer.controller;

import aiss.gitminer.dto.ErrorResponse;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.services.CommentService;
import aiss.gitminer.services.IssueService;
import aiss.gitminer.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
    @SuppressWarnings("ConstantConditions")
    public List<Issue> getIssues(
            @Parameter(
                    description = "Issue page number",
                    schema =  @Schema(minimum = "0")
            )
            @RequestParam(defaultValue = "0")
            Integer issuePage,

            @Parameter(
                    description = "Issue page size",
                    schema = @Schema(minimum = "0")
            )
            @RequestParam(defaultValue = "30")
            Integer issuePageSize,

            @Parameter(
                    description = "Order issues by field",
                    schema = @Schema(
                            allowableValues = {"id", "-id", "createdAt", "-createdAt", "updatedAt", "-updatedAt","title", "-title","votes", "-votes"}
                    )
            )
            @RequestParam(defaultValue = "-createdAt")
            String issueOrderBy,

            @Parameter(
                    description = "Comment page number",
                    schema = @Schema(minimum = "0")
            )
            @RequestParam(defaultValue = "0")
            Integer commentPage,

            @Parameter(
                    description = "Comment page size",
                    schema = @Schema(minimum = "0")
            )
            @RequestParam(defaultValue = "30")
            Integer commentPageSize,

            @Parameter(
                    description = "Order comments by field",
                    schema = @Schema(
                            allowableValues = {"id", "-id", "createdAt", "-createdAt", "updatedAt", "-updatedAt", "body", "-body"}
                    )
            )
            @RequestParam(defaultValue = "-createdAt")
            String commentOrderBy,

            @Parameter(
                    description = "Issue author ID",
                    schema = @Schema(minLength = 1)
            ) @RequestParam(required = false)
            String authorId,

            @Parameter(
                    description = "Issue state",
                    schema = @Schema(minLength = 1)
            ) @RequestParam(required = false) String state
    ) {
        ValidationUtils.accumulateValidation(ValidationUtils::validateIssuePage, issuePage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateIssuePageSize, issuePageSize);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPage, commentPage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPageSize, commentPageSize);
        ValidationUtils.accumulateValidation(ValidationUtils::validateUserId, authorId);
        ValidationUtils.accumulateValidation(ValidationUtils::validateIssueState, state);
        Issue.Order issueOrder = ValidationUtils.accumulateValidationReturn(Issue.Order::new, issueOrderBy);
        Comment.Order commentOrder = ValidationUtils.accumulateValidationReturn(Comment.Order::new, commentOrderBy);
        ValidationUtils.throwIfErrors();
        Pageable pageIssue = issueOrder.getPageable(issuePage, issuePageSize);
        Pageable pageComment = commentOrder.getPageable(commentPage, commentPageSize);
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
    @SuppressWarnings("ConstantConditions")
    public Issue getIssueById(
            @Parameter(
                    description = "Searched issue ID",
                    schema = @Schema(minLength = 1)
            )
            @PathVariable
            String id,

            @Parameter(
                    description = "Comment page number",
                    schema = @Schema(minimum = "0")
            )
            @RequestParam(defaultValue = "0")
            Integer commentPage,

            @Parameter(
                    description = "Comment page size",
                    schema = @Schema(minimum = "0")
            )
            @RequestParam(defaultValue = "30")
            Integer commentPageSize,

            @Parameter(
                    description = "Order comments by field",
                    schema = @Schema(
                            allowableValues = {"id", "-id", "createdAt", "-createdAt", "updatedAt", "-updatedAt", "body", "-body"}
                    )
            )
            @RequestParam(defaultValue = "-createdAt")
            String commentOrderBy
    ) {
        ValidationUtils.accumulateValidation(ValidationUtils::validateIssueId, id);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPage, commentPage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPageSize, commentPageSize);
        Comment.Order commentOrder = ValidationUtils.accumulateValidationReturn(Comment.Order::new, commentOrderBy);
        ValidationUtils.throwIfErrors();
        Pageable pageComment = commentOrder.getPageable(commentPage, commentPageSize);
        return issueService.findIssueById(id, pageComment);
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
    @SuppressWarnings("ConstantConditions")
    public List<Comment> getCommentsByIssueId(
            @Parameter(
                    description = "Id of the issue whose comments are being requested",
                    schema = @Schema(minLength = 1)
            )
            @PathVariable
            String id,

            @Parameter(
                    description = "Comment page number",
                    schema = @Schema(minimum = "0")
            )
            @RequestParam(defaultValue = "0")
            Integer commentPage,

            @Parameter(
                    description = "Comment page size",
                    schema = @Schema(minimum = "0")
            )
            @RequestParam(defaultValue = "30")
            Integer commentPageSize,

            @Parameter(
                    description = "Order comments by field",
                    schema = @Schema(
                            allowableValues = {"id", "-id", "createdAt", "-createdAt", "updatedAt", "-updatedAt", "body", "-body"}
                    )
            )
            @RequestParam(defaultValue = "-createdAt")
            String commentOrderBy
    ) {
        ValidationUtils.accumulateValidation(ValidationUtils::validateIssueId, id);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPage, commentPage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPageSize, commentPageSize);
        Comment.Order commentOrder = ValidationUtils.accumulateValidationReturn(Comment.Order::new, commentOrderBy);
        ValidationUtils.throwIfErrors();
        Pageable pageComment = commentOrder.getPageable(commentPage, commentPageSize);
        return commentService.findByIssueId(id,pageComment);
    }

}