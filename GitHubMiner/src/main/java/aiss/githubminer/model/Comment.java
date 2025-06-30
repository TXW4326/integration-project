
package aiss.githubminer.model;

import aiss.githubminer.utils.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

    @JsonProperty("id")
    @NotNull(message = "Comment ID cannot be null")
    @NotBlank(message = "Comment id should not be empty")
    private String id;

    @JsonProperty("author")
    @Valid
    private User author;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @NotNull(message = "Commit creation date cannot be null")
    @Past(message = "Commit creation date must be in the past")
    private LocalDateTime created_at;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @NotNull(message = "Commit update date cannot be null")
    @Past(message = "Commit update date must be in the past")
    private LocalDateTime updated_at;

    @JsonProperty("body")
    @NotNull(message = "Comment body cannot be null")
    @NotEmpty(message = "Comment body cannot be empty")
    private String body;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("author")
    public User getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(User author) {
        this.author = author;
    }

    @JsonProperty("created_at")
    public LocalDateTime getCreated_at() {
        return created_at;
    }

    @JsonProperty("created_at")
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @JsonProperty("updated_at")
    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    @JsonProperty("updated_at")
    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    @JsonProperty("body")
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("body", body)
                .append("created_at", created_at)
                .append("updated_at", updated_at)
                .append("author", author)
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
