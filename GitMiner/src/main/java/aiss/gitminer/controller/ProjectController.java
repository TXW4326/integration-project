package aiss.gitminer.controller;

import aiss.gitminer.dto.ErrorResponse;
import aiss.gitminer.model.Project;
import aiss.gitminer.services.ProjectService;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(
        name = "Project",
        description = "Project public API"
)
@ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    private ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // API Documentation:
    // GET http://localhost:8080/gitminer/projects
    @Operation(
            summary = "Get a list of projects",
            description = "Get a list of projects",
            tags = {"GET", "getAll", "Project"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Project.class)), mediaType = "application/json"))
    })
    // API Route and method:
    @GetMapping
    public List<Project> getProjects(
            @Parameter(description = "Project page number, default is 0") @RequestParam(defaultValue = "0") Integer projectPage,
            @Parameter(description = "Project page size, default is 30") @RequestParam(defaultValue = "30") Integer projectPageSize,
            @Parameter(description = "Issue page number, default is 0") @RequestParam(defaultValue = "0") Integer issuePage,
            @Parameter(description = "Issue page size, default is 30") @RequestParam(defaultValue = "30") Integer issuePageSize,
            @Parameter(description = "Commit page number, default is 0") @RequestParam(defaultValue = "0") Integer commitPage,
            @Parameter(description = "Commit page size, default is 30") @RequestParam(defaultValue = "30") Integer commitPageSize,
            @Parameter(description = "Comment page number, default is 0") @RequestParam(defaultValue = "0") Integer commentPage,
            @Parameter(description = "Comment page size, default is 30") @RequestParam(defaultValue = "30") Integer commentPageSize
    ) {
        Pageable pageProjects = PageRequest.of(projectPage, projectPageSize);
        Pageable pageIssues = PageRequest.of(issuePage, issuePageSize);
        Pageable pageCommits = PageRequest.of(commitPage, commitPageSize);
        Pageable pageComments = PageRequest.of(commentPage, commentPageSize);
        return projectService.findAllProjects(pageProjects,pageComments,pageCommits,pageIssues);
    }

    // API Documentation:
    // Get http://localhost:8080/gitminer/projects/{id}
    @Operation(
            summary = "Get project by ID",
            description = "Get project by ID",
            tags = {"GET", "getById", "Project"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })

    // API Route and method:
    @GetMapping("/{id}")
    public Project getProjectById(
            @Parameter(description = "Searched project ID") @PathVariable String id,
            @Parameter(description = "Issue page number, default is 0") @RequestParam(defaultValue = "0") Integer issuePage,
            @Parameter(description = "Issue page size, default is 30") @RequestParam(defaultValue = "30") Integer issuePageSize,
            @Parameter(description = "Commit page number, default is 0") @RequestParam(defaultValue = "0") Integer commitPage,
            @Parameter(description = "Commit page size, default is 30") @RequestParam(defaultValue = "30") Integer commitPageSize,
            @Parameter(description = "Comment page number, default is 0") @RequestParam(defaultValue = "0") Integer commentPage,
            @Parameter(description = "Comment page size, default is 30") @RequestParam(defaultValue = "30") Integer commentPageSize
    ) {
        Pageable pageIssues = PageRequest.of(issuePage, issuePageSize);
        Pageable pageCommits = PageRequest.of(commitPage, commitPageSize);
        Pageable pageComments = PageRequest.of(commentPage, commentPageSize);
        return projectService.findProjectById(id, pageIssues, pageComments, pageCommits);
    }

    // API Documentation:
    // POST http://localhost:8080/gitminer/projects/
    @Operation(
            summary = "Insert a new project",
            description = "Insert a new project into the database given its data as a parameter in JSON",
            tags = {"POST", "Project"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))

    })
    // API Route and method:
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project createProject(@Valid @RequestBody Project project) {
        return projectService.save(project);
    }
}