package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repositories.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(
        name = "Project",
        description = "Project public API"
)
@RestController
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    // API Documentation:
    // GET http://localhost:8080/gitminer/projects
    @Operation(
            summary = "Get a list of projects",
            description = "Get a list of projects",
            tags = {"GET", "getAll", "Projects", "Project"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content (schema = @Schema())})
    })

    // API Route and method:
    @GetMapping("/projects")
    public List<Project> getProjects() {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            throw new ProjectNotFoundException("No projects found");
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
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content (schema = @Schema())})

    })

    // API Route and method:
    @GetMapping("/projects/{id}")
    public Project getProjectById(@Parameter(description = "Searched project ID")@PathVariable String id) {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new ProjectNotFoundException("Project not found");
        }
        return project.get();
    }

    // API Documentation:
    // POST http://localhost:8080/gitminer/projects/
    @Operation(
            summary = "Insert a new project",
            description = "Insert a new project into the database given its data as a parameter in JSON",
            tags = {"POST", "insert", "Project"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema())})
    })
    // API Route and method:
    @PostMapping("/projects")
    public ResponseEntity createProject(@Valid @RequestBody Project p) {
        Project projectToBePosted = new Project();
        projectToBePosted.setId(p.getId());
        projectToBePosted.setName(p.getName());
        projectToBePosted.setIssues(p.getIssues());
        projectToBePosted.setCommits(p.getCommits());
        projectToBePosted.setPullRequests(p.getPullRequests());

        Project savedProject = projectRepository.save(projectToBePosted);

        return new ResponseEntity(savedProject, HttpStatus.CREATED);
    }
}