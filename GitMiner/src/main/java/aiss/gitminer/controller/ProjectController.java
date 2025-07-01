package aiss.gitminer.controller;

import aiss.gitminer.dto.ErrorResponse;
import aiss.gitminer.exception.BadRequestException;
import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repositories.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(
        name = "Project",
        description = "Project public API"
)
@ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {
    private final ProjectRepository projectRepository;

    @Autowired
    private ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
    public List<Project> getProjects() {
        List<Project> projects = projectRepository.findAll();
        //TODO: Why not return an empty list instead of throwing an exception?
        if (projects.isEmpty()) {
            throw new ProjectNotFoundException();
        }
        return projects;
    }

    // API Documentation:
    // Get http://localhost:8080/gitminer/projects/{id}
    @Operation(
            summary = "Get project by ID",
            description = "Get project by ID",
            tags = {"GET", "getById", "Project"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json"))
    })

    // API Route and method:
    @GetMapping("/{id}")
    public Project getProjectById(@Parameter(description = "Searched project ID") @PathVariable String id) {
        if (id.isBlank()) throw new BadRequestException("Project ID cannot be blank");
        Optional<Project> project = projectRepository.findById(id);
        if (project.isEmpty()) throw new ProjectNotFoundException();
        return project.get();
    }

    // API Documentation:
    // POST http://localhost:8080/gitminer/projects/
    @Operation(
            summary = "Insert a new project",
            description = "Insert a new project into the database given its data as a parameter in JSON",
            tags = {"POST", "Project"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json"))
    })
    // API Route and method:
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project createProject(@Valid @RequestBody Project project) {
        return projectRepository.save(project);
    }
}