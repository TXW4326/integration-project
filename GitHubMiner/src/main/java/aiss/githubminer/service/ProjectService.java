package aiss.githubminer.service;

import aiss.githubminer.model.Commit;
import aiss.githubminer.model.Issue;
import aiss.githubminer.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;

    public Project getProject(String owner, String repo, Integer sinceCommits, Integer sinceIssues, Integer maxPagess) {
        String url = "repos/" + owner + "/" + repo;
        Project project = GitHubAPIService.get(url, Project.class, restTemplate);
        return project;
    }

}
