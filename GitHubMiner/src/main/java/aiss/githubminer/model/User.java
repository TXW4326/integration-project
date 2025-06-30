
package aiss.githubminer.model;


import aiss.githubminer.utils.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("username")
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @JsonProperty("id")
    @NotNull(message = "User ID cannot be null")
    @Min(value = 0, message = "User ID must be a non-negative integer")
    private long id;

    @JsonProperty("avatar_url")
    @NotNull(message = "Avatar URL cannot be null")
    @URL(message = "Invalid URL format for avatar URL")
    private String avatar_url;

    @JsonProperty("web_url")
    @NotNull(message = "Web URL cannot be null")
    @URL(message = "Invalid URL format for web URL")
    private String web_url;

    @JsonProperty("name")
    @Size(min = 1, message = "User real name cannot be empty")
    private String name;


    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("login")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("id")
    public String getIdAsString() {
        return String.valueOf(id);
    }

    @JsonProperty("avatar_url")
    public String getAvatar_url() {
        return avatar_url;
    }

    @JsonProperty("avatar_url")
    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    @JsonProperty("web_url")
    public String getWeb_url() {
        return web_url;
    }

    @JsonProperty("html_url")
    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("username", username)
                .append("name", name)
                .append("avatar_url", avatar_url)
                .append("web_url", web_url)
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
