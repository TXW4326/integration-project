
package aiss.githubminer.model;


import aiss.githubminer.utils.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    @JsonProperty("id")
    @Min(value = 0, message = "Project ID must be a non-negative integer")
    private long id;

    @JsonProperty("name")
    @NotNull(message = "The name of the project cannot be null")
    @NotBlank(message = "The name of the project cannot be empty")
    private String name;

    @JsonProperty("web_url")
    @NotNull(message = "The URL of the project cannot be null")
    @URL(message = "Invalid URL format for project web URL")
    private String web_url;

    @Valid
    @JsonProperty("commits")
    @NotNull(message = "Project commit list cannot be null")
    @UniqueElements(message = "Commit ids should be unique")
    private List<@NotNull(message = "Project commit cannot be null") Commit> commits;

    @Valid
    @JsonProperty("issues")
    @NotNull(message = "Project issue list cannot be null")
    @UniqueElements(message = "Issue ids should be unique")
    private List<@NotNull(message = "Project issue cannot be null") Issue> issues;

    @JsonIgnore
    public long getId() { return id; }

    @JsonProperty("id")
    public String getIdAsString() {
        return String.valueOf(id);
    }

    @JsonProperty("id")
    public void setId(long id) {
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

    @JsonProperty("html_url")
    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    @JsonProperty("issues")
    public List<Issue> getIssues() {
        return issues;
    }

    @JsonProperty("issues")
    public void setIssues (List<Issue> issues) { this.issues = issues;}

    @JsonProperty("commits")
    public List<Commit> getCommits() {
        return commits;
    }

    @JsonProperty("commits")
    public void setCommits(List<Commit> commits) {
        this.commits = commits;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Issue issue)) return false;
        return getId() == issue.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
