package aiss.gitminer.controller;

import aiss.gitminer.dto.ErrorResponse;
import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repositories.CommentRepository;
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
        name = "Comment",
        description = "Comment public API"
)
@ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
@RestController
@RequestMapping("/gitminer/comments")
public class CommentController {

    private final CommentRepository commentRepository;

    @Autowired
    private CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
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
    public List<Comment> getComments() {
        List<Comment> comments = commentRepository.findAll();
        if (comments.isEmpty()) throw new CommentNotFoundException();
        return comments;
    }

    // API Documentation:
    // Get http://localhost:8080/gitminer/comments/{id}
    @Operation(
            summary = "Get comment by ID",
            description = "Get comment by ID",
            tags = {"GET", "getById", "Comment"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json"))
    })
    // API Route and method:
    @GetMapping("/{id}")
    public Comment getCommentById(@Parameter(description = "Searched comment ID")@PathVariable String id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) throw new CommentNotFoundException();
        return comment.get();
    }
}
