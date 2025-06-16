
package aiss.githubminer.model;

import aiss.githubminer.service.GitHubAPIService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Map;

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

    @JsonProperty("commit")
    private void unpackCommit(Map<String, Object> commit) {
        Object authorObj = commit.get("author");
        if (authorObj == null) {return;}
        if (!(authorObj instanceof Map<?, ?> authorMap)) {
            //TODO: Handle error more gracefully
            throw new IllegalArgumentException("Author object must be of type Map");
        }
        this.author_name = authorMap.get("name").toString();
        this.author_email = authorMap.get("email").toString();
        this.authored_date = LocalDateTime.parse(authorMap.get("date").toString().replace("Z", ""));
        String[] description =  ((String) commit.get("message")).split("\n\n",2);
        this.title = description[0];
        if (description.length > 1) {this.message = description[1];}
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("node_id")
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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Commit.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("web_url");
        sb.append('=');
        sb.append(((this.web_url == null)?"<null>":this.web_url));
        sb.append(',');
        sb.append("author_name");
        sb.append('=');
        sb.append(((this.author_name == null)?"<null>":this.author_name));
        sb.append(',');
        sb.append("author_email");
        sb.append('=');
        sb.append(((this.author_email == null)?"<null>":this.author_email));
        sb.append(',');
        sb.append("authored_date");
        sb.append('=');
        sb.append(((this.authored_date == null)?"<null>":this.authored_date));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("message");
        sb.append('=');
        sb.append(((this.message == null)?"<null>":this.message));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
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
}
