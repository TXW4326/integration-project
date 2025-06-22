
package aiss.githubminer.model;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.utils.JsonUtils;
import aiss.githubminer.utils.ToStringBuilder;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    @JsonProperty("id")
    private String id;

    @JsonProperty("votes")
    private int votes;

    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private User author;

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
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    private PageInfo pageInfoComments;

    @JsonSetter("reactions")
    private void unpackReactions(Map<String, ?> reactions) {
        if (reactions == null || !reactions.containsKey("votes")) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Issue data does not contain votes");
        }
        this.votes = (int) reactions.get("votes");
    }

    @JsonProperty("votes")
    public int getVotes() {
        return votes;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
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
    private void setLabels(Map<String, List<Map<String,String>>> labels) {
        if (labels == null || !labels.containsKey("nodes")) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Issue data does not contain labels");
        }
        this.labels = labels.get("nodes").stream()
                .map(label -> label.get("name"))
                .toList();
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

    @JsonSetter("assignee")
    public void setAssignee(Map<String, List<User>> assignees) {
        if (assignees != null && assignees.containsKey("nodes") && !assignees.get("nodes").isEmpty()) {
            this.assignee = assignees.get("nodes").get(0);
        }
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

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("comments")
    public List<Comment> getComments() {
        return comments;
    }

    @JsonSetter("comments")
    public void setComments(Map<String, ?> comments) {
        if (comments == null || !comments.containsKey("pageInfo")) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Comments data does not contain pageInfo");
        }
        this.pageInfoComments = JsonUtils.convertToObject(comments.get("pageInfo"), PageInfo.class);
        if (!comments.containsKey("nodes") || !(comments.get("nodes") instanceof List<?> nodes)) return;
        this.comments = JsonUtils.convertToObject(
                nodes,
                new TypeReference<>() {}
        );
    }

    @JsonIgnore
    public void addComments(List<Comment> comments) {
        if (comments != null) {
            this.comments.addAll(comments);
        }
    }

    @JsonIgnore
    public PageInfo getPageInfoComments() {
        return pageInfoComments;
    }

    @JsonIgnore
    public void setPageInfoComments(PageInfo pageInfoComments) {
        this.pageInfoComments = pageInfoComments;
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
