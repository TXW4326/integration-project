package aiss.gitminer.controller;

import aiss.gitminer.exception.PullRequestNotFoundException;
import aiss.gitminer.model.PullRequest;
import aiss.gitminer.repositories.PullRequestRepository;
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
        name = "PullRequest",
        description = "PullRequest public API"
)
@RestController
public class PullRequestController {

    @Autowired
    PullRequestRepository pullRequestRepository;

    // API Documentation:
    // GET http://localhost:8080/gitminer/pullrequests
    @Operation(
            summary = "Get a list of pull requests",
            description = "Get a list of pull requests",
            tags = {"GET", "getAll", "PullRequests", "PullRequest"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PullRequest.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content (schema = @Schema())})
    })

    // API Route and method:
    @GetMapping("/pullrequests")
    public List<PullRequest> getPullRequests() {
        List<PullRequest> pullRequests = pullRequestRepository.findAll();
        if (pullRequests.isEmpty()) {
            throw new PullRequestNotFoundException("No pull requests found");
        }
        return pullRequests;
    }

    // API Documentation:
    // Get http://localhost:8080/gitminer/pullrequests/{id}
    @Operation(
            summary = "Get pull request by ID",
            description = "Get pull request by ID",
            tags = {"GET", "getById", "PullRequest"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PullRequest.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content (schema = @Schema())})

    })
    @GetMapping("/pullrequests/{id}")
    public PullRequest getPullRequestById(@Parameter(description = "Searched pull request ID")@PathVariable String id) {
        Optional<PullRequest> pullRequest = pullRequestRepository.findById(id);
        if (!pullRequest.isPresent()) {
            throw new PullRequestNotFoundException("Pull Request not found");
        }
        return pullRequest.get();
    }
}