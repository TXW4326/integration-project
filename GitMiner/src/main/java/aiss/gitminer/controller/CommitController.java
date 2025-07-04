package aiss.gitminer.controller;

import aiss.gitminer.dto.ErrorResponse;
import aiss.gitminer.model.Commit;
import aiss.gitminer.services.CommitService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Commit",
        description = "Commit public API"
)
@ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {

    private final CommitService commitService;

    @Autowired
    private CommitController(CommitService commitService) {
        this.commitService = commitService;
    }

    // API Documentation:
    // GET http://localhost:8080/gitminer/commits
    @Operation(
            summary = "Get a list of commits",
            description = "Get a list of commits",
            tags = {"GET", "getAll", "Commits", "Commit"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Commit.class)), mediaType = "application/json"))
    })

    // API Route and method:
    @GetMapping
    @SuppressWarnings("ConstantConditions")
    public List<Commit> getCommits(
            @Parameter(
                    description = "Commit page number",
                    schema =  @Schema(minimum = "0")
            )
            @RequestParam(defaultValue = "0")
            Integer commitPage,

            @Parameter(
                    description = "Commit page size",
                    schema = @Schema(minimum = "0")
            )
            @RequestParam(defaultValue = "30")
            Integer commitPageSize,

            @Parameter(
                    description = "Order commits by field",
                    schema = @Schema(
                            allowableValues = {"id", "-id", "title", "-title"}
                    )
            )
            @RequestParam(defaultValue = "id")
            String commitOrderBy
    ) {
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommitPage, commitPage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommitPageSize, commitPageSize);
        Commit.Order commitOrder = ValidationUtils.accumulateValidationReturn(Commit.Order::new, commitOrderBy);
        ValidationUtils.throwIfErrors();
        return commitService.findAll(commitOrder.getPageable(commitPage, commitPageSize));
    }

    // API Documentation:
    // Get http://localhost:8080/gitminer/commits/{id}
    @Operation(
            summary = "Get commit by ID",
            description = "Get commit by ID",
            tags = {"GET", "getById", "Commit"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Commit.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")),
    })
    // API Route and method:
    @GetMapping("/{id}")
    public Commit getCommitById(
            @Parameter(
                    description = "Searched commit ID",
                    schema = @Schema(
                            minLength = 40,
                            maxLength = 40
                    )
            )
            @PathVariable
            String id
    ) {
        return commitService.findById(id);
    }
}