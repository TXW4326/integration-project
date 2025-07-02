package aiss.bitbucketminer.service;

import aiss.bitbucketminer.models.Issue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BitbucketIssueService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final BitbucketCommentService commentService = new BitbucketCommentService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Issue> fetchIssues(String workspace, String repo, int limit, int maxPages) {
        List<Issue> issues = new ArrayList<>();
        String url = "https://api.bitbucket.org/2.0/repositories/" + workspace + "/" + repo + "/issues?pagelen=50";

        for (int i = 0; i < maxPages && url != null && issues.size() < limit; i++) {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            List<Map<String, Object>> values = (List<Map<String, Object>>) response.get("values");

            for (Map<String, Object> rawIssue : values) {
                Issue issue = objectMapper.convertValue(rawIssue, Issue.class);

                List comments = commentService.fetchComments(workspace, repo, String.valueOf(rawIssue.get("id")));
                issue.setComments(comments);

                issues.add(issue);
                if (issues.size() >= limit) break;
            }

            url = (String) response.get("next");
        }

        return issues;
    }
}