package aiss.bitbucketminer.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Author {
    @JsonProperty("uuid")
    private String id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("display_name")
    private String name;

    @JsonProperty("links")
    private Avatar avatarWrapper;

    //author y user debería ser lo mismo

    public String getAvatar_url() {
        return avatarWrapper != null ? avatarWrapper.getAvatar().getHref() : null;
    }

    public String getWeb_url() {
        return null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    static class Avatar {
        private AvatarImage avatar;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Data
        static class AvatarImage {
            private String href;
        }
    }
}