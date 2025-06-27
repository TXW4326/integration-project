package aiss.gitminer.controller;

import aiss.gitminer.dto.ErrorResponse;
import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repositories.CommitRepository;
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
        name = "Commit",
        description = "Commit public API"
)
@ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {

    private final CommitRepository commitRepository;

    @Autowired
    private CommitController(CommitRepository commitRepository) {
        this.commitRepository = commitRepository;
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
    public List<Commit> getCommits() {
        List<Commit> commits = commitRepository.findAll();
        //TODO: Why not return an empty list instead of throwing an exception?
        if (commits.isEmpty()) throw new CommitNotFoundException();
        return commits;
    }

    // API Documentation:
    // Get http://localhost:8080/gitminer/commits/{id}
    @Operation(
            summary = "Get commit by ID",
            description = "Get commit by ID",
            tags = {"GET", "getById", "Commit"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Commit.class), mediaType = "application/json"))
    })
    // API Route and method:
    @GetMapping("/{id}")
    public Commit getCommitById(@Parameter(description = "Searched commit ID")@PathVariable String id) {
        Optional<Commit> commit = commitRepository.findById(id);
        if (commit.isEmpty()) throw new CommitNotFoundException();
        return commit.get();
    }
}