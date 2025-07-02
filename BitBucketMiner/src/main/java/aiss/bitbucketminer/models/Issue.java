package aiss.bitbucketminer.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({ "id", "title", "description",
        "state", "created_at", "updated_at",
        "labels", "author", "comments",
        "votes"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    private String id;

    private String title;

    @JsonProperty("description")
    public String getDescription() {
        if( content == null ) return "No description";
        return content.description;
    }

    @JsonIgnore
    private RawContent content;

    @JsonIgnore
    public RawContent getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(RawContent content) {
        this.content = content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RawContent {
        @JsonProperty("description")
        private String description;

        @JsonProperty("raw")
        public void setRaw(String cadena) {
            this.description = cadena;
        }
    }

    @JsonProperty("state")
    private String state;

    @JsonAlias("created_on")
    @JsonProperty("created_at")
    private String created_at;

    @JsonAlias("updated_on")
    @JsonProperty("updated_at")
    private String updated_at;

    @JsonProperty("labels")
    private List<String> labels = new ArrayList<>();

    @JsonProperty("reporter")
    private Author author;

    @JsonProperty("comments")
    private List<Comment> comments = new ArrayList<>();

    @JsonProperty("votes")
    private Integer votes;

    @JsonProperty(access = Access.WRITE_ONLY)
    private Author assignee;

}
