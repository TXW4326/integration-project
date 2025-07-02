package aiss.bitbucketminer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Comment {
    @JsonProperty("id")
    private String id;

    @JsonProperty("body")
    private String body;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("author")
    private User author;
}
