
package aiss.githubminer.model;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.utils.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {

    @JsonProperty("id")
    @NotNull(message = "Commit ID cannot be null")
    @NotBlank(message = "Commit ID cannot be empty")
    @Size(min = 40, max = 40, message = "Commit ID must be exactly 40 characters long")
    private String id;

    @JsonProperty("author_name")
    @Size(min = 1, message = "Commit author name cannot be empty")
    private String author_name;

    @JsonProperty("author_email")
    @Email(message = "Invalid email format for author email")
    private String author_email;

    @JsonProperty("authored_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Past(message = "Authored date must be in the past")
    private LocalDateTime authored_date;

    @JsonProperty("message")
    private String message;

    @JsonProperty("title")
    @NotNull(message = "Commit title cannot be null")
    private String title;

    @JsonProperty("web_url")
    @NotNull(message = "Commit web URL cannot be null")
    @URL(message = "Invalid URL format for commit web URL")
    private String web_url;

    @JsonProperty("commit")
    private void unpackCommit(LinkedHashMap<String, ?> commit) {
        if (commit == null || !commit.containsKey("author")) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Commit data does not contain author information: " + commit);
        }
        Object authorObj = commit.get("author");
        if (authorObj == null) return;
        if (!(authorObj instanceof LinkedHashMap<?, ?> authorMap)) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid author data format in commit: " + commit);
        }
        if (!authorMap.containsKey("name") || !authorMap.containsKey("email") || !authorMap.containsKey("date")) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Author data is missing required fields: " + authorMap);
        }
        this.author_name = authorMap.get("name").toString();
        this.author_email = authorMap.get("email").toString();
        try {
            this.authored_date = LocalDateTime.parse(authorMap.get("date").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        } catch (DateTimeParseException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Commit authored date is invalid: " + authorMap.get("date"));
        }
        String[] description =  ((String) commit.get("message")).split("\n\n",2);
        this.title = description[0];
        if (description.length > 1) {this.message = description[1];}
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("sha")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("web_url")
    public String getWeb_url() {
        return web_url;
    }

    @JsonProperty("html_url")
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Commit commit)) return false;
        return Objects.equals(getId(), commit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
