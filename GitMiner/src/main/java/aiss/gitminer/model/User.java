
package aiss.gitminer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "GMUser")     // Watch out: User is a reserved keyword in H2
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Represents a User")
public class User {

    @Id
    @JsonProperty("id")
    @NotNull(message = "User ID cannot be null")
    @NotBlank(message = "User ID cannot be empty")
    @Schema(
        description = "Unique identifier for the user",
        example = "MDQ6VXNlcjQ5MDQ4NA==",
        minLength = 1,
        required = true
    )
    private String id;

    @JsonProperty("username")
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be empty")
    @Schema(
        description = "Username of the user",
        example = "johndoe",
        minLength = 1,
        required = true
    )
    private String username;

    @JsonProperty("name")
    @Size(min = 1, message = "User real name cannot be empty")
    @Schema(
        description = "Real name of the user",
        example = "John Doe",
        minLength = 1
    )
    private String name;

    @JsonProperty("avatar_url")
    @NotNull(message = "Avatar URL cannot be null")
    @URL(message = "Invalid URL format for avatar URL")
    @Schema(
        description = "URL of the user's avatar",
        example = "https://avatars.githubusercontent.com/u/490484?v=4",
        format = "uri",
        required = true
    )
    private String avatarUrl;

    @JsonProperty("web_url")
    @NotNull(message = "User Web URL cannot be null")
    @URL(message = "Invalid URL format for user web URL")
    @Schema(
        description = "Web URL of the user profile",
        example = "https://github.com/johndoe",
        format = "uri",
        required = true
    )
    private String webUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(User.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("username");
        sb.append('=');
        sb.append(((this.username == null)?"<null>":this.username));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("avatarUrl");
        sb.append('=');
        sb.append(((this.avatarUrl == null)?"<null>":this.avatarUrl));
        sb.append(',');
        sb.append("webUrl");
        sb.append('=');
        sb.append(((this.webUrl == null)?"<null>":this.webUrl));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
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
