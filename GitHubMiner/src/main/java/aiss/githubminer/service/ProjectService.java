package aiss.githubminer.service;

import aiss.githubminer.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    GitHubAPIService gitHubAPIService;

    @Autowired
    IssueService issueService;

    @Autowired
    CommitService commitService;

    public Project getProject(String owner, String repo, Integer sinceCommits, Integer sinceIssues, Integer maxPages) {
        Project project = gitHubAPIService.get("repos/{owner}/{repo}", Project.class, owner, repo);
        //TODO: Handle errors from user input (sinceCommits, sinceIssues, maxPages)
        project.setCommits(commitService.getCommits(owner,repo,sinceCommits,maxPages));
        project.setIssues(issueService.getIssues(owner,repo,sinceIssues, maxPages));
        return project;
    }

}
