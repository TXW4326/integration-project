package aiss.bitbucketminer.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    private String id;

    private String title;

    @JsonProperty("content")
    private RawContent content;

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

    @JsonProperty("links")
    private Links links;

    public String getDescription() {
        return content != null ? content.getRaw() : null;
    }

    public String getUrl() {
        return links != null && links.html != null ? links.html.href : null;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RawContent {
        private String raw;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {
        private Href html;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Href {
        private String href;
    }
}
