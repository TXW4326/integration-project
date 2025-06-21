package aiss.githubminer.service;


import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Project;
import aiss.githubminer.model.Variables;
import aiss.githubminer.utils.JsonUtils;
import aiss.githubminer.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class GitHubAPIService {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    final int PER_PAGE;
    private final String GITHUB_API_URL;
    private final String TOKEN;
    private final RestTemplate restTemplate;
    private final Map<String,?> BODY;

    @Autowired
    public GitHubAPIService(@Value("${github.api.token:}") String TOKEN,
                            @Value("${github.api.url:}") String GITHUB_API_URL,
                            @Value("${github.default.perPage}") int PER_PAGE,
                            @Value("classpath:query.graphql") Resource resourceQuery,
                            RestTemplate restTemplate) {
        this.TOKEN = TOKEN;
        this.restTemplate = restTemplate;
        this.GITHUB_API_URL = GITHUB_API_URL;
        this.PER_PAGE = PER_PAGE;
        try {
            this.BODY = Map.of("query", resourceQuery.getContentAsString(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to load GraphQL query from resources");
        }}


    public Map<String, ?> sendGraphQLQuery(Variables variables) {
        HttpHeaders headers = new HttpHeaders();
        if (TOKEN != null && !TOKEN.isEmpty()) {
            headers.set("Authorization", "Bearer " + TOKEN);
        }
        Map<String, Object> body = new HashMap<>(BODY);
        body.put("variables", variables);
        HttpEntity<Map<String, ?>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, ?>> response = restTemplate.exchange(GITHUB_API_URL, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, ?>>() {});
        return response.getBody();
    }

    private void validateUserInput(String owner, String repo, int sinceCommits, int sinceIssues, int maxPages) {
        ValidationUtils.validateOwnerAndRepo(owner, repo);
        ValidationUtils.validateSinceCommits(sinceCommits);
        ValidationUtils.validateSinceIssues(sinceIssues);
        ValidationUtils.validateMaxPages(maxPages);
    }

    private int[] optimiceMaxPages(int maxPages) {
        int numObjects = PER_PAGE * maxPages;
        return new int[] {numObjects / 100, numObjects % 100};
    }

    public Project getProject(String owner, String repo, int sinceCommits, int sinceIssues, int maxPages) {
        validateUserInput(owner, repo, sinceCommits, sinceIssues, maxPages);
        LocalDateTime now = LocalDateTime.now();
        String resultCommits = now.minusDays(sinceCommits).format(formatter);
        String resultIssues = now.minusDays(sinceIssues).format(formatter);
        int[] maxPagesOptimized = optimiceMaxPages(maxPages);
        Map<String,?> parameters = Map.of(
                "owner", owner,
                "repo", repo,
                "sinceCommits", sinceCommits,
                "sinceIssues", sinceIssues,
                "maxPages", maxPages
        );
        Variables variables = new Variables(owner, repo, resultCommits, resultIssues, true, 100,100,100);
        Project project;
        for (int page = 1; page <= maxPagesOptimized[0]; page++) {
            Map<String, ?> response = sendGraphQLQuery(variables);
            Map<String,?> repository = ValidationUtils.validateGraphQLresponse(response, parameters);
            if (variables.isFetchRepoDetails()) {
                project = JsonUtils.convertToObject(repository, Project.class);
                variables.setFetchRepoDetails(false);
            } else {


            }

        }
        return null;
    }

}
