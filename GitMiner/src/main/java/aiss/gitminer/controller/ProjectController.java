package aiss.gitminer.controller;

import aiss.gitminer.dto.ErrorResponse;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import aiss.gitminer.services.ProjectService;
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
    @SuppressWarnings("ConstantConditions")
    public List<Project> getProjects(
            @Parameter(
                    description = "Project page number",
                    schema = @Schema(minimum = "0")
            )
            @RequestParam(defaultValue = "0")
            Integer projectPage,

            @Parameter(
                    description = "Project page size",
                    schema = @Schema(minimum = "0")
            ) @RequestParam(defaultValue = "30")
            Integer projectPageSize,

            @Parameter(
                    description = "Order ",
                    schema = @Schema(
                            allowableValues = {"id", "-id", "name", "-name"}
                    )
            )
            @RequestParam(defaultValue = "id")
            String orderProjectBy,

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
            String commitOrderBy,

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
        ValidationUtils.accumulateValidation(ValidationUtils::validateProjectPage, projectPage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateProjectPageSize, projectPageSize);
        ValidationUtils.accumulateValidation(ValidationUtils::validateIssuePage, issuePage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateIssuePageSize, issuePageSize);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommitPage, commitPage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommitPageSize, commitPageSize);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPage, commentPage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPageSize, commentPageSize);
        Issue.Order issueOrder = ValidationUtils.accumulateValidationReturn(Issue.Order::new, issueOrderBy);
        Commit.Order commitOrder = ValidationUtils.accumulateValidationReturn(Commit.Order::new, commitOrderBy);
        Comment.Order commentOrder = ValidationUtils.accumulateValidationReturn(Comment.Order::new, commentOrderBy);
        Project.Order projectOrder = ValidationUtils.accumulateValidationReturn(Project.Order::new, orderProjectBy);
        ValidationUtils.throwIfErrors();
        Pageable pageIssues = issueOrder.getPageable(issuePage, issuePageSize);
        Pageable pageCommits = commitOrder.getPageable(commitPage, commitPageSize);
        Pageable pageComments = commentOrder.getPageable(commentPage, commentPageSize);
        Pageable pageProjects = projectOrder.getPageable(projectPage, projectPageSize);
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
    @SuppressWarnings("ConstantConditions ")
    public Project getProjectById(
            @Parameter(
                    description = "Searched project ID",
                    schema = @Schema(
                            example = "MDEwOlJlcG9zaXRvcnkxMTQ4NzUz",
                            minLength = 1
                    )
            )
            @PathVariable
            String id,

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
            String commitOrderBy,

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
        ValidationUtils.accumulateValidation(ValidationUtils::validateProjectId, id);
        ValidationUtils.accumulateValidation(ValidationUtils::validateIssuePage, issuePage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateIssuePageSize, issuePageSize);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommitPage, commitPage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommitPageSize, commitPageSize);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPage, commentPage);
        ValidationUtils.accumulateValidation(ValidationUtils::validateCommentPageSize, commentPageSize);
        Issue.Order issueOrder = ValidationUtils.accumulateValidationReturn(Issue.Order::new, issueOrderBy);
        Commit.Order commitOrder = ValidationUtils.accumulateValidationReturn(Commit.Order::new, commitOrderBy);
        Comment.Order commentOrder = ValidationUtils.accumulateValidationReturn(Comment.Order::new, commentOrderBy);
        ValidationUtils.throwIfErrors();
        Pageable pageIssues = issueOrder.getPageable(issuePage, issuePageSize);
        Pageable pageCommits = commitOrder.getPageable(commitPage, commitPageSize);
        Pageable pageComments = commentOrder.getPageable(commentPage, commentPageSize);
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