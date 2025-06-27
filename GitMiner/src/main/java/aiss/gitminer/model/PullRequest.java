package aiss.gitminer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name= "PullRequest")
public class PullRequest {
    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("state")
    private String state;

    @JsonProperty("title")
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonProperty("user")
    private User user;

    @JsonProperty("body")
    private String body;

    @JsonProperty("originBranch")
    private String originBranch;

    @JsonProperty("targetBranch")
    private String targetBranch;

    @JsonProperty("createdAt")
    @NotEmpty(message = "CreatedAt must be defined.")
    private String createdAt;

    @JsonProperty("updatedAt")
    private String  updatedAt;


    public PullRequest() {}


    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOriginBranch() {
        return originBranch;
    }

    public void setOriginBranch(String originBranch) {
        this.originBranch = originBranch;
    }

    @Override
    public String toString() {
        return "PullRequest{" +
                "id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", title='" + title + '\'' +
                ", user=" + user +
                ", body='" + body + '\'' +
                ", originBranch='" + originBranch + '\'' +
                ", targetBranch='" + targetBranch + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
