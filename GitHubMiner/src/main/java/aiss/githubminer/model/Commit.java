
package aiss.githubminer.model;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.utils.ToStringBuilder;
import com.fasterxml.jackson.annotation.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {

    @JsonProperty("id")
    private String id;

    @JsonProperty("author_name")
    private String author_name;

    @JsonProperty("author_email")
    private String author_email;

    @JsonProperty("authored_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime authored_date;

    @JsonProperty("message")
    private String message;

    @JsonProperty("title")
    private String title;

    @JsonProperty("web_url")
    private String web_url;

    @JsonSetter("author")
    private void unpackAuthor(LinkedHashMap<String, String> author) {
        if (author == null || author.isEmpty()) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Commit data does not contain author data.");
        }
        if (!author.containsKey("author_name") || !author.containsKey("author_email")) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Commit author data is incomplete.");
        }
        this.author_name = author.get("author_name");
        this.author_email = author.get("author_email");
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("web_url")
    public String getWeb_url() {
        return web_url;
    }

    @JsonProperty("web_url")
    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    @JsonProperty("author_name")
    public String getAuthor_name() {
        return author_name;
    }

    @JsonProperty("author_name")
    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    @JsonProperty("author_email")
    public String getAuthor_email() {
        return author_email;
    }

    @JsonProperty("author_email")
    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    @JsonProperty("authored_date")
    public LocalDateTime getAuthored_date() {
        return authored_date;
    }

    @JsonProperty("authored_date")
    public void setAuthored_date(LocalDateTime authored_date) {
        this.authored_date = authored_date;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        if (message.isEmpty()) return;
        this.message = message;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("message", message)
                .append("author_name", author_name)
                .append("author_email", author_email)
                .append("authored_date", authored_date)
                .append("web_url", web_url)
                .toString();
    }
}
