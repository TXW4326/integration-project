
package aiss.githubminer.model;

import aiss.githubminer.utils.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Variables {

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("name")
    private String name;

    @JsonProperty("resultCommits")
    private String resultCommits;

    @JsonProperty("resultIssues")
    private String resultIssues;

    @JsonProperty("fetchRepoDetails")
    private boolean fetchRepoDetails;

    @JsonProperty("numIssues")
    private int numIssues;

    @JsonProperty("numCommits")
    private int numCommits;

    @JsonProperty("numComments")
    private int numComments;

    @JsonProperty("commitCursor")
    private String commitCursor;

    @JsonProperty("issueCursor")
    private String issueCursor;

    @JsonProperty("commentCursor")
    private String commentCursor;

    @JsonProperty("owner")
    public String getOwner() {
        return owner;
    }

    @JsonProperty("owner")
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("resultCommits")
    public String getResultCommits() {
        return resultCommits;
    }

    @JsonProperty("resultCommits")
    public void setResultCommits(String resultCommits) {
        this.resultCommits = resultCommits;
    }

    @JsonProperty("resultIssues")
    public String getResultIssues() {
        return resultIssues;
    }

    @JsonProperty("resultIssues")
    public void setResultIssues(String resultIssues) {
        this.resultIssues = resultIssues;
    }

    @JsonProperty("fetchRepoDetails")
    public boolean isFetchRepoDetails() {
        return fetchRepoDetails;
    }

    @JsonProperty("fetchRepoDetails")
    public void setFetchRepoDetails(boolean fetchRepoDetails) {
        this.fetchRepoDetails = fetchRepoDetails;
    }

    @JsonProperty("numIssues")
    public int getNumIssues() {
        return numIssues;
    }

    @JsonProperty("numIssues")
    public void setNumIssues(int numIssues) {
        this.numIssues = numIssues;
    }

    @JsonProperty("numCommits")
    public int getNumCommits() {
        return numCommits;
    }

    @JsonProperty("numCommits")
    public void setNumCommits(int numCommits) {
        this.numCommits = numCommits;
    }

    @JsonProperty("numComments")
    public int getNumComments() {
        return numComments;
    }

    @JsonProperty("numComments")
    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    @JsonProperty("commitCursor")
    public String getCommitCursor() {
        return commitCursor;
    }

    @JsonProperty("commitCursor")
    public void setCommitCursor(String commitCursor) {
        this.commitCursor = commitCursor;
    }

    @JsonProperty("issueCursor")
    public String getIssueCursor() {
        return issueCursor;
    }

    @JsonProperty("issueCursor")
    public void setIssueCursor(String issueCursor) {
        this.issueCursor = issueCursor;
    }

    @JsonProperty("commentCursor")
    public String getCommentCursor() {
        return commentCursor;
    }

    @JsonProperty("commentCursor")
    public void setCommentCursor(String commentCursor) {
        this.commentCursor = commentCursor;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("owner", owner)
                .append("name", name)
                .append("resultCommits", resultCommits)
                .append("resultIssues", resultIssues)
                .append("fetchRepoDetails", fetchRepoDetails)
                .append("numIssues", numIssues)
                .append("numCommits", numCommits)
                .append("numComments", numComments)
                .append("commitCursor", commitCursor)
                .append("issueCursor", issueCursor)
                .append("commentCursor", commentCursor)
                .toString();
    }

    public Variables(String owner, String name, String resultCommits, String resultIssues,
                     boolean fetchRepoDetails, int numIssues, int numCommits, int numComments) {
        this.owner = owner;
        this.name = name;
        this.resultCommits = resultCommits;
        this.resultIssues = resultIssues;
        this.fetchRepoDetails = fetchRepoDetails;
        this.numIssues = numIssues;
        this.numCommits = numCommits;
        this.numComments = numComments;
    }

}
