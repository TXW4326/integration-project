package aiss.githubminer.utils;

import aiss.githubminer.model.Issue;
import aiss.githubminer.model.Project;
import aiss.githubminer.model.Variables;
import aiss.githubminer.service.GitHubAPIService;

import java.util.Map;

public final class ProjectIterator {

    private Project project;
    private final Variables variables;
    private int fetchedElements = 0;
    private final int elements;
    private QueryBuilder queryBuilder;
    private final GitHubAPIService gitHubAPIService;
    private final Map<String, ?> parameters;

    public ProjectIterator(int elements, Variables variables, GitHubAPIService gitHubAPIService, Map<String,?> parameters) {
        this.variables = variables;
        this.elements = elements;
        this.gitHubAPIService = gitHubAPIService;
        this.parameters = parameters;
    }


    public boolean hasNext() {
        return project == null || (fetchedElements < elements && (project.getPageInfoCommits().isHasNextPage() || project.getPageInfoIssues().isHasNextPage()))
                || project.getIssues().parallelStream().anyMatch(i->i.getPageInfoComments().isHasNextPage() && i.getComments().size() < elements);
    }

    public void next() {
        int next = Math.min(elements - fetchedElements, 100);
        if (project == null) {
            if (next > 0) {
                variables.setNumCommits(next);
                variables.setNumIssues(next);
                variables.setNumComments(next);
                variables.setFetchCommits(true);
                variables.setFetchIssues(true);
            }
            Map<String, ?> response = gitHubAPIService.sendGraphQLQuery(variables);
            Map<String,?> repository = ValidationUtils.validateGraphQLresponse(response, parameters);
            project = JsonUtils.convertToObject(repository, Project.class);
            queryBuilder = new QueryBuilder(project, elements);
            variables.setFetchRepoDetails(false);
            fetchedElements += next;
            return;
        }
        if (project.getPageInfoCommits().isHasNextPage() && fetchedElements < elements) {
            variables.setNumCommits(next);
            variables.setCommitCursor(project.getPageInfoCommits().getEndCursor());
        } else {
            variables.setFetchCommits(false);
        }
        if (project.getPageInfoIssues().isHasNextPage() && fetchedElements < elements) {
            variables.setNumIssues(next);
            variables.setIssueCursor(project.getPageInfoIssues().getEndCursor());
        } else {
            variables.setFetchIssues(false);
        }
        Map<String, ?> response = gitHubAPIService.sendGraphQLQuery(queryBuilder.buildCommentsQuery(),variables);
        Map<String,?> repository = ValidationUtils.validateGraphQLresponse(response, parameters);
        Project project2 = JsonUtils.convertToObject(repository, Project.class);
        project.addCommits(project2.getCommits());
        project.addIssues(project2.getIssues());
        project.setPageInfoCommits(project2.getPageInfoCommits());
        project.setPageInfoIssues(project2.getPageInfoIssues());
        repository.entrySet().stream().parallel().filter(entry -> entry.getKey().contains("issue_"))
                .forEach(entry -> {
                    int issueIndex = Integer.parseInt(entry.getKey().split("_")[1]);
                    Issue issue = project.getIssues().get(issueIndex);
                    Issue issueTMP = JsonUtils.convertToObject(entry.getValue(), Issue.class);
                    issue.addComments(issueTMP.getComments());
                    issue.setPageInfoComments(issueTMP.getPageInfoComments());
                });
        fetchedElements += next;
    }

    public Project getProject() {
        return project;
    }
}
