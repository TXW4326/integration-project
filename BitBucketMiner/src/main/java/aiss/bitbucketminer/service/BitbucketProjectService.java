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
}

