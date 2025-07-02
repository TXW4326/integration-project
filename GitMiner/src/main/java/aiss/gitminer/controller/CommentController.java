package aiss.gitminer.controller;

import aiss.gitminer.dto.ErrorResponse;
import aiss.gitminer.model.Comment;
import aiss.gitminer.services.CommentService;
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
        name = "Comment",
        description = "Comment public API"
)
@ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
@RestController
@RequestMapping("/gitminer/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    private CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // API Documentation:
    // GET http://localhost:8080/gitminer/comments
    @Operation(
        summary = "Get a list of comments",
        description = "Get a list of comments",
        tags = {"GET", "getAll", "Comments", "Comment"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Comment.class)), mediaType = "application/json"))
    })

    // API Route and method:
    @GetMapping
    @SuppressWarnings("ConstantConditions")
    public List<Comment> getComments(
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
                    description = "Author id of the comments",
                    schema = @Schema(minLength = 1)
            )
            @RequestParam(required = false)
            String authorId
    ) {
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPage, commentPage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPageSize, commentPageSize);
        ValidationUtils.accumulateValidation(ValidationUtils::validateUserId, authorId);
        Comment.Order commentOrder = ValidationUtils.accumulateValidationReturn(Comment.Order::new, commentOrderBy);
        ValidationUtils.throwIfErrors();
        Pageable pageComment = commentOrder.getPageable(commentPage, commentPageSize);
        return authorId == null ? commentService.findAll(pageComment) : commentService.findAllByAuthorId(authorId, pageComment);
    }

    // API Documentation:
    // Get http://localhost:8080/gitminer/comments/{id}
    @Operation(
            summary = "Get comment by ID",
            description = "Get comment by ID",
            tags = {"GET", "getById", "Comment"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })
    // API Route and method:
    @GetMapping("/{id}")
    public Comment getCommentById(
            @Parameter(
                    description = "Searched comment ID",
                    schema = @Schema(minLength = 1)
            )
            @PathVariable
            String id
    ) {
        return commentService.findById(id);
    }
}
