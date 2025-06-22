package aiss.githubminer.utils;

import aiss.githubminer.model.Issue;
import aiss.githubminer.model.Project;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class QueryBuilder {

    private final Project project;
    private final int elements;
    private static final String defaultQuery =
                """
                {{issueName}}: issues(first: 1, {{operation}}: {{issueCursor}}, filterBy: {since: $resultIssues, states: OPEN}, orderBy: {field: CREATED_AT, direction: DESC}) {
                        comments(first: {{numComments}}, after: {{commentCursor}}) {
                                nodes {
                                    id
                                    body
                                    created_at: createdAt
                                    updated_at: updatedAt
                                    author {
                                        ... on User {
                                            ...UserFields
                                        }
                                    }
                                }
                                pageInfo {
                                    ...PageFields
                                }
                        }
                }
                """;

    public QueryBuilder(Project project, int elements) {
        this.project = project;
        this.elements = elements;
    }
    
    public String buildCommentsQuery() {
        return IntStream.range(0, project.getIssues().size())
                .filter(numIssue-> project.getIssues().get(numIssue).getPageInfoComments().isHasNextPage()
                        && project.getIssues().get(numIssue).getComments().size() < elements)
                .limit(10)
                .mapToObj(this::buildQuery)
                .collect(Collectors.joining("\n"));
    }

    private String buildQuery(int numIssue) {
        String issueCursor;
        String operation;
        if (numIssue == project.getIssues().size() - 1) {
            if (numIssue == 0) {
                issueCursor = "";
            } else {
                issueCursor = project.getIssues().get(numIssue - 1).getPageInfoComments().getEndCursor();
            }
            operation = "after";
        } else {
            operation = "before";
            issueCursor = project.getIssues().get(numIssue + 1).getPageInfoComments().getEndCursor();
        }
        Issue issue = project.getIssues().get(numIssue);
        return defaultQuery
                .replace("{{issueName}}", "issue_" + numIssue)
                .replace("{{issueCursor}}", issueCursor)
                .replace("{{operation}}", operation)
                .replace("{{numComments}}", String.valueOf(Math.min(100, elements - issue.getComments().size())))
                .replace("{{commentCursor}}",issue.getPageInfoComments().getEndCursor());
    }
}
