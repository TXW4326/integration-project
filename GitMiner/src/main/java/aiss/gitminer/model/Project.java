
package aiss.gitminer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "Project")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Represents a project with its details, including commits and issues.")
public class Project {

    @JsonIgnore
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Id
    @JsonProperty("id")
    @NotNull(message = "The id of the project cannot be null")
    @NotBlank(message = "The id of the project cannot be empty")
    @Schema(
            description = "Unique identifier for the project",
            example = "MDEwOlJlcG9zaXRvcnkxMTQ4NzUz",
            minLength = 1,
            required = true
    )
    public String id;

    @JsonProperty("name")
    @NotNull(message = "The name of the project cannot be null")
    @NotBlank(message = "The name of the project cannot be empty")
    @Schema(
            description = "Name of the project",
            example = "spring-framework",
            minLength = 1,
            required = true
    )
    private String name;

    @JsonProperty("web_url")
    @NotNull(message = "The URL of the project cannot be null")
    @URL(message = "Invalid URL format for project web URL")
    @Schema(
            description = "Web URL of the project",
            example = "https://github.com/spring-projects/spring-framework",
            format = "uri",
            required = true)
    public String webUrl;

    @Valid
    @JsonProperty("commits")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId")
    @NotNull(message = "Project commit list cannot be null")
    @UniqueElements(message = "Commit ids should be unique")
    @ArraySchema(
            schema = @Schema(implementation = Commit.class),
            arraySchema = @Schema(
                    description = "List of commits associated with the project",
                    required = true
            ),
            uniqueItems = true
    )
    private List<@NotNull(message = "Project commit cannot be null") Commit> commits;

    @JsonProperty("issues")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId")
    @Valid
    @NotNull(message = "Project issue list cannot be null")
    @UniqueElements(message = "Issue ids should be unique")
    @ArraySchema(
            schema = @Schema(implementation = Issue.class),
            arraySchema = @Schema(
                    description = "List of issues associated with the project",
                    required = true
            ),
            uniqueItems = true
    )
    private List<@NotNull(message = "Project issue cannot be null") Issue> issues;

    public Project() {
        commits = new ArrayList<>();
        issues = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Project.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("web_url");
        sb.append('=');
        sb.append(((this.webUrl == null)?"<null>":this.webUrl));
        sb.append(',');
        sb.append("commits");
        sb.append('=');
        sb.append(((this.commits == null)?"<null>":this.commits));
        sb.append(',');
        sb.append("issues");
        sb.append('=');
        sb.append(((this.issues == null)?"<null>":this.issues));
        sb.append(',');

        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Project project)) return false;
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
