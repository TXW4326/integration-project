package aiss.gitminer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Commit")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Represents a commit in a Git repository")
public class Commit {

    @JsonIgnore
    @Schema(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

    @Id
    @JsonProperty("id")
    @NotNull(message = "Commit ID cannot be null")
    @NotBlank(message = "Commit ID cannot be empty")
    @Size(min = 40, max = 40, message = "Commit ID must be exactly 40 characters long")
    @Schema(
            description = "SHA of the commit",
            example = "b256babad56e64316f62535279b3a6e0fe960513",
            required = true
    )
    private String id;

    @JsonProperty("title")
    @NotNull(message = "Commit title cannot be null")
    @Schema(
            description = "Title of the commit",
            example = "Fix issue with user authentication",
            required = true
    )
    private String title;

    @JsonProperty("message")
    @Column(columnDefinition="TEXT")
    @Schema(
            description = "Detailed message of the commit",
            example = "This commit fixes the issue with user authentication that was causing login failures."
    )
    private String message;

    @JsonProperty("author_name")
    //@NotEmpty(message = "Commit author name cannot be empty.") This annotation does a null check, and at least in GitHub if a user is private, the real name is null.
    @Size(min = 1, message = "Commit author name cannot be empty")
    @Schema(
            description = "Name of the author of the commit",
            example = "John Doe"
    )
    private String authorName;

    @JsonProperty("author_email")
    @Email(message = "Invalid email format for author email")
    @Schema(
            description = "Email of the author of the commit",
            example = "john.doe@broadcom.com",
            format = "email"
    )
    private String authorEmail;

    @JsonProperty("authored_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Past(message = "Authored date must be in the past")
    @Schema(
            description = "Date and time when the commit was authored",
            example = "2023-10-03T12:00:00Z",
            format = "date-time",
            pattern = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$",
            minLength = 20,
            maxLength = 20
    )
    private LocalDateTime authoredDate;

    @JsonProperty("web_url")
    @NotNull(message = "Commit web URL cannot be null")
    @URL(message = "Invalid URL format for commit web URL")
    @Schema(
            description = "Web URL of the commit",
            example = "https://github.com/spring-projects/spring-framework/commit/b256babad56e64316f62535279b3a6e0fe960513",
            format = "uri",
            required = true
    )
    private String webUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public LocalDateTime getAuthoredDate() {
        return authoredDate;
    }

    public void setAuthoredDate(LocalDateTime authoredDate) {
        this.authoredDate = authoredDate;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Commit.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null) ? "<null>" : this.id));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null) ? "<null>" : this.title));
        sb.append(',');
        sb.append("message");
        sb.append('=');
        sb.append(((this.message == null) ? "<null>" : this.message));
        sb.append(',');
        sb.append("authorName");
        sb.append('=');
        sb.append(((this.authorName == null) ? "<null>" : this.authorName));
        sb.append(',');
        sb.append("authorEmail");
        sb.append('=');
        sb.append(((this.authorEmail == null) ? "<null>" : this.authorEmail));
        sb.append(',');
        sb.append("authoredDate");
        sb.append('=');
        sb.append(((this.authoredDate == null) ? "<null>" : this.authoredDate));
        sb.append(',');
        sb.append("webUrl");
        sb.append('=');
        sb.append(((this.webUrl == null) ? "<null>" : this.webUrl));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
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
