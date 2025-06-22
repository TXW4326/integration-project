
package aiss.githubminer.model;


import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.utils.JsonUtils;
import aiss.githubminer.utils.ToStringBuilder;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("web_url")
    private String web_url;

    @JsonProperty("commits")
    private List<Commit> commits = new ArrayList<>();

    @JsonProperty("issues")
    private List<Issue> issues = new ArrayList<>();

    @JsonIgnore
    private PageInfo pageInfoCommits;

    @JsonIgnore
    private PageInfo pageInfoIssues;

    @JsonProperty("id")
    public String getId() { return id; }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("web_url")
    public String getWeb_url() {
        return web_url;
    }

    @JsonProperty("web_url")
    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    @JsonProperty("issues")
    public List<Issue> getIssues() {
        return issues;
    }

    @JsonSetter("issues")
    public void setIssues (Map<String, ?> issues) {
        if (issues == null || !issues.containsKey("pageInfo")) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Project data does not contain issues pageInfo");
        }
        this.pageInfoIssues = JsonUtils.convertToObject(issues.get("pageInfo"), PageInfo.class);
        if (!issues.containsKey("nodes") || !(issues.get("nodes") instanceof List<?> nodes)) return;
        this.issues = JsonUtils.convertToObject(
                nodes,
                new TypeReference<>() {});
    }

    @JsonIgnore
    public void addIssues (List<Issue> issues) {
        if (issues != null) {
            this.issues.addAll(issues);
        }
    }

    @JsonProperty("commits")
    public List<Commit> getCommits() {
        return commits;
    }


    @JsonSetter("commits")
    public void setCommits(Map<String, Map<String, Map<String, ?>>> commits) {
        if (commits == null || !commits.containsKey("target")) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Project data does not contain target attribute on commits");
        }
        Map<String, Map<String, ?>> target = commits.get("target");
        if (target == null || !target.containsKey("history")) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Project data does not contain history attribute on targets from commits");
        }
        Map<String, ?> history = target.get("history");
        if (history == null || !history.containsKey("pageInfo")) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Project data does not contain history pageInfo of commits");
        }
        this.pageInfoCommits = JsonUtils.convertToObject(history.get("pageInfo"), PageInfo.class);
        Object nodesObj = history.get("nodes");
        if (nodesObj instanceof List<?> nodes && !nodes.isEmpty()) {
            this.commits = JsonUtils.convertToObject(nodes, new TypeReference<>() {});
        }
    }

    @JsonIgnore
    public void addCommits(List<Commit> commits) {
        if (commits != null) {
            this.commits.addAll(commits);
        }
    }

    @JsonIgnore
    public PageInfo getPageInfoCommits() {
        return pageInfoCommits;
    }

    @JsonIgnore
    public void setPageInfoCommits(PageInfo pageInfoCommits) {
        if (pageInfoCommits != null) this.pageInfoCommits = pageInfoCommits;
    }

    @JsonIgnore
    public PageInfo getPageInfoIssues() {
        return pageInfoIssues;
    }

    @JsonIgnore
    public void setPageInfoIssues(PageInfo pageInfoIssues) {
        if (pageInfoIssues != null) this.pageInfoIssues = pageInfoIssues;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("web_url", web_url)
                .append("commits", commits)
                .append("issues", issues)
                .toString();
    }

}
