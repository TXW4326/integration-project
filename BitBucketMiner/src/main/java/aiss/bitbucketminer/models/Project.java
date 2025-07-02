package aiss.bitbucketminer.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Project {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("web_url")
    private String web_url;

    @JsonProperty("commits")
    private List<Commit> commits = new ArrayList<>();

    @JsonProperty("issues")
    private List<Issue> issues = new ArrayList<>();

    public Project(String name, String web_url) {
        this.id = String.valueOf(name.hashCode());
        this.name = name;
        this.web_url = web_url;
    }

    public Project() {}
}
