package aiss.bitbucketminer.service;

import aiss.bitbucketminer.models.Commit;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BitbucketCommitService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Commit> fetchCommits(String workspace, String repo, int limit, int maxPages) {
        List<Commit> commits = new ArrayList<>();
        String url = "https://api.bitbucket.org/2.0/repositories/" + workspace + "/" + repo + "/commits?pagelen=50";

        for (int i = 0; i < maxPages && url != null && commits.size() < limit; i++) {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            List<Map<String, Object>> values = (List<Map<String, Object>>) response.get("values");

            for (Map<String, Object> rawCommit : values) {
                Commit commit = objectMapper.convertValue(rawCommit, Commit.class);
                commits.add(commit);
                if (commits.size() >= limit) break;
            }

            url = (String) response.get("next");
        }

        return commits;
    }
}