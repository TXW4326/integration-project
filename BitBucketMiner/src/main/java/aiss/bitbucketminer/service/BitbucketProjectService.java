package aiss.bitbucketminer.service;

import aiss.bitbucketminer.models.Commit;
import aiss.bitbucketminer.models.Issue;
import aiss.bitbucketminer.models.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BitbucketProjectService {

    private final BitbucketCommitService commitService;
    private final BitbucketIssueService issueService;

    public Project fetchProject(String workspace, String repo, int nCommits, int nIssues, int maxPages) {
        Project project = new Project(repo, "https://bitbucket.org/" + workspace + "/" + repo);
        List<Commit> commits = commitService.fetchCommits(workspace, repo, nCommits, maxPages);
        List<Issue> issues = issueService.fetchIssues(workspace, repo, nIssues, maxPages);
        project.getCommits().addAll(commits);
        project.getIssues().addAll(issues);
        return project;
    }

//    private final RestTemplate restTemplate = new RestTemplate();
//    public List<Project> fetchAllProjects(String workspace, int nCommits, int nIssues, int maxPages) {
//        List<Project> allProjects = new ArrayList<>();
//        String url = "https://api.bitbucket.org/2.0/repositories/" + workspace;
//
//        while (url != null) {
//            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//            List<Map<String, Object>> values = (List<Map<String, Object>>) response.get("values");
//
//            for (Map<String, Object> repoMap : values) {
//                String repoName = (String) repoMap.get("name");
//                if (repoName != null) {
//                    Project project = fetchProject(workspace, repoName, nCommits, nIssues, maxPages);
//                    allProjects.add(project);
//                }
//            }
//
//            url = (String) response.get("next");
//        }
//
//        return allProjects;
//    }
    //GET postman un poco raro en comparación con el de GitHub... solo un project??

}
