package aiss.githubminer.controller;


import aiss.githubminer.model.Project;
import aiss.githubminer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/github")
public class GitHubMinerController {

    private final ProjectService projectService;
    private final Integer DEFAULT_SINCE_COMMITS;
    private final Integer DEFAULT_SINCE_ISSUES;
    private final Integer DEFAULT_MAX_PAGES;

    @Autowired
    public GitHubMinerController(ProjectService projectService,
                                 @Value("${github.default.sinceCommits:}") Integer defaultSinceCommits,
                                 @Value("${github.default.sinceIssues:}") Integer defaultSinceIssues,
                                 @Value("${github.default.maxPages:}") Integer defaultMaxPages) {
        this.projectService = projectService;
        this.DEFAULT_SINCE_COMMITS = defaultSinceCommits;
        this.DEFAULT_SINCE_ISSUES = defaultSinceIssues;
        this.DEFAULT_MAX_PAGES = defaultMaxPages;
    }

    @GetMapping("/{username}/{repo}")
    public Project getProject(
        @PathVariable String username,
        @PathVariable String repo,
        @RequestParam(required = false) Integer sinceCommits,
        @RequestParam(required = false) Integer sinceIssues,
        @RequestParam(required = false) Integer maxPages
    ) {
        return projectService.getProject(
                username,
                repo,
                sinceCommits != null ? sinceCommits : DEFAULT_SINCE_COMMITS,
                sinceIssues != null ? sinceIssues : DEFAULT_SINCE_ISSUES,
                maxPages != null ? maxPages : DEFAULT_MAX_PAGES);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{username}/{repo}")
    public Project sendProjectToGitMiner(
        @PathVariable String username,
        @PathVariable String repo,
        @RequestParam(required = false) Integer sinceCommits,
        @RequestParam(required = false) Integer sinceIssues,
        @RequestParam(required = false) Integer maxPages
    ) {
        Project project = projectService.getProject(
                username,
                repo,
                sinceCommits != null ? sinceCommits : DEFAULT_SINCE_COMMITS,
                sinceIssues != null ? sinceIssues : DEFAULT_SINCE_ISSUES,
                maxPages != null ? maxPages : DEFAULT_MAX_PAGES);
        projectService.sendProject(project);
        return project;
    }
}
