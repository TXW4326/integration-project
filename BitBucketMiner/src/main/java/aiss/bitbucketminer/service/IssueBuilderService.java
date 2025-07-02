package aiss.bitbucketminer.service;

import aiss.bitbucketminer.models.Comment;
import aiss.bitbucketminer.models.Issue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class IssueBuilderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "https://api.bitbucket.org/2.0/repositories";

    public Issue buildIssue(Issue rawIssue, String workspace, String repoSlug) {
        String issueId = rawIssue.getId();
        List<Comment> comments = fetchComments(workspace, repoSlug, issueId);
        rawIssue.setComments(comments);
        return rawIssue;
    }

    private List<Comment> fetchComments(String workspace, String repoSlug, String issueId) {
        List<Comment> comments = new ArrayList<>();
        try {
            String url = String.format("%s/%s/%s/issues/%s/comments", BASE_URL, workspace, repoSlug, issueId);

            HttpHeaders headers = new HttpHeaders();
            // headers.set("Authorization", "Bearer YOUR_TOKEN"); // if auth is needed
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
            JsonNode values = response.getBody().get("values");

            if (values != null && values.isArray()) {
                comments = objectMapper.convertValue(values, new TypeReference<>() {});
            }
        } catch (Exception e) {
            System.err.println("Error fetching comments for issue " + issueId + ": " + e.getMessage());
        }
        return comments;
    }
}
