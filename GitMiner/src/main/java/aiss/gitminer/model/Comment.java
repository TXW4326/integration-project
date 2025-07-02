
package aiss.gitminer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Comment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Represents a comment in the system")
public class Comment {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issueId")
    @Schema(hidden = true)
    private Issue issue;

    @Id
    @JsonProperty("id")
    @NotNull(message = "Comment ID cannot be null")
    @NotBlank(message = "Comment id should not be empty")
    @Schema(
            description = "Unique identifier for the comment",
            example = "IC_kwDOABGHUc60Kmi0",
            minLength = 1,
            required = true
    )
    private String id;

    @JsonProperty("body")
    @Column(columnDefinition="TEXT")
    @NotNull(message = "Comment body cannot be null")
    @NotEmpty(message = "Comment body cannot be empty")
    @Schema(
            description = "Content of the comment",
            example = "This is a sample comment body.",
            minLength = 1,
            required = true
    )
    private String body;

    @Valid
    @JsonProperty("author")
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @OneToOne(cascade=CascadeType.ALL)
    private User author;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @NotNull(message = "Comment creation date cannot be null")
    @Past(message = "Comment creation date must be in the past")
    @Schema(
            description = "Date and time when the comment was created",
            example = "2023-10-03T12:00:00Z",
            format = "date-time",
            pattern = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$",
            minLength = 20,
            maxLength = 20
    )
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @NotNull(message = "Comment update date cannot be null")
    @Past(message = "Comment update date must be in the past")
    @Schema(
            description = "Date and time when the comment was last updated",
            example = "2023-10-03T12:00:00Z",
            format = "date-time",
            pattern = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$",
            minLength = 20,
            maxLength = 20
    )
    private LocalDateTime updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Comment.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null) ? "<null>" : this.id));
        sb.append(',');
        sb.append("body");
        sb.append('=');
        sb.append(((this.body == null) ? "<null>" : this.body));
        sb.append(',');
        sb.append("author");
        sb.append('=');
        sb.append(((this.author == null) ? "<null>" : this.author));
        sb.append(',');
        sb.append("createdAt");
        sb.append('=');
        sb.append(((this.createdAt == null) ? "<null>" : this.createdAt));
        sb.append(',');
        sb.append("updatedAt");
        sb.append('=');
        sb.append(((this.updatedAt == null) ? "<null>" : this.updatedAt));
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
