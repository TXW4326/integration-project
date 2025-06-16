package aiss.githubminer.service;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Project;
import aiss.githubminer.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

@Service
public class ProjectService {

    private final GitHubAPIService gitHubAPIService;
    private final IssueService issueService;
    private final CommitService commitService;
    private final RestTemplate restTemplate;
    private final String gitMinerApiUrl;

    @Autowired
    public ProjectService(GitHubAPIService gitHubAPIService,
                          IssueService issueService,
                          CommitService commitService,
                          RestTemplate restTemplate,
                          @Value("${gitminer.api.url}") String gitMinerApiUrl) {
        this.gitHubAPIService = gitHubAPIService;
        this.issueService = issueService;
        this.commitService = commitService;
        this.restTemplate = restTemplate;
        this.gitMinerApiUrl = gitMinerApiUrl;
    }

    public Project getProject(String owner, String repo, int sinceCommits, int sinceIssues, int maxPages) {
        userInputValidation(owner, repo, sinceCommits, sinceIssues, maxPages);
        Project project;
        try {
            project = gitHubAPIService.get("repos/{owner}/{repo}", Project.class, owner, repo);
        } catch (HttpStatusCodeException e) {
            //Java 17 doesn't support switch expressions with Sealed Interfaces, so we switch over the value of the status code
            switch (e.getStatusCode().value()) {
                case 404: throw new GitHubMinerException(HttpStatus.NOT_FOUND, "Project not found: " + owner + "/" + repo);
                case 301: throw new GitHubMinerException(HttpStatus.MOVED_PERMANENTLY, "Project has been moved: " + owner + "/" + repo);
                case 403: throw new GitHubMinerException(HttpStatus.FORBIDDEN, "Access to the project is forbidden: " + owner + "/" + repo);
                default: throw new GitHubMinerException(e.getStatusCode(), "An error occurred while fetching the project: " + owner + "/" + repo);
            }
        } catch (UnknownHttpStatusCodeException e) {
            throw new GitHubMinerException(e.getStatusCode(), "An unknown error occurred while fetching the project: " + owner + "/" + repo);
        } catch (RuntimeException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching the project: " + owner + "/" + repo);
        }
        project.setCommits(commitService.getCommitsInternal(owner, repo, sinceCommits, maxPages));
        project.setIssues(issueService.getIssuesInternal(owner, repo, sinceIssues, maxPages));
        return project;
    }

    public void sendProject(Project project) {
        //TODO: Handle errors
        restTemplate.postForEntity(gitMinerApiUrl, project, Project.class);
    }

    private static void userInputValidation(String owner, String repo, int sinceCommits, int sinceIssues, int maxPages) {
        ValidationUtils.validateOwnerAndRepo(owner, repo);
        ValidationUtils.validateSinceCommits(sinceCommits);
        ValidationUtils.validateSinceIssues(sinceIssues);
        ValidationUtils.validateMaxPages(maxPages);
    }

}
