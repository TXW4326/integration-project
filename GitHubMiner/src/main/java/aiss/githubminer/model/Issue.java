
package aiss.githubminer.model;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.utils.ToStringBuilder;
import com.fasterxml.jackson.annotation.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    @JsonProperty("id")
    private long id;

    @JsonProperty("votes")
    private int votes;

    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private User author;

    @JsonIgnore
    private int number;

    @JsonProperty("labels")
    private List<String> labels;

    @JsonProperty("state")
    private String state;

    @JsonProperty("assignee")
    private User assignee;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime created_at;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updated_at;

    @JsonProperty("closed_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime closed_at;

    @JsonProperty("description")
    private String description;

    // Although the attribute appears to be ignored here, its getter is nevertheless invoked when deserializing the Issue object.
    @JsonIgnore
    private List<Comment> comments;

    @JsonProperty("reactions")
    private void unpackReactions(LinkedHashMap<String, ?> reactions) {
        if (reactions == null || !reactions.containsKey("total_count")) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Reactions data does not contain total_count: " + reactions);
        }
        this.votes = (int) reactions.get("total_count");
    }

    @JsonProperty("votes")
    public int getVotes() {
        return votes;
    }

    @JsonIgnore
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public String getIdAsString() {
        return String.valueOf(id);
    }

    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("author")
    public User getAuthor() {
        return author;
    }

    @JsonProperty("user")
    public void setAuthor(User author) {
        this.author = author;
    }

    @JsonProperty("labels")
    public List<String> getLabels() {
        return labels;
    }


    @JsonSetter("labels")
    private void unpackLabels(List<?> labels) {
        this.labels = labels.parallelStream()
                .map(l -> {
                    if (l instanceof LinkedHashMap) {
                        return (String) ((LinkedHashMap<?, ?>) l).get("name");
                    } else if (l instanceof String) {
                        return (String) l;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("assignee")
    public User getAssignee() {
        return assignee;
    }

    @JsonProperty("assignee")
    public void setAssignee(User assignee) {
        this.assignee = assignee;
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

    @JsonProperty("closed_at")
    public LocalDateTime getClosed_at() {
        return closed_at;
    }

    @JsonProperty("closed_at")
    public void setClosed_at(LocalDateTime closed_at) {
        this.closed_at = closed_at;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("body")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public int getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(int number) {
        this.number = number;
    }

    @JsonProperty("comments")
    public List<Comment> getComments() {
        return comments;
    }

    @JsonIgnore
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("description", description)
                .append("state", state)
                .append("created_at", created_at)
                .append("updated_at", updated_at)
                .append("closed_at", closed_at)
                .append("labels", labels)
                .append("votes", votes)
                .append("assignee", assignee)
                .append("author", author)
                .append("comments", comments)
                .toString();
    }
}
