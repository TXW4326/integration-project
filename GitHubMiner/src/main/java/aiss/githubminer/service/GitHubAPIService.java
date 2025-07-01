package aiss.githubminer.service;


import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Project;
import aiss.githubminer.model.Variables;
import aiss.githubminer.utils.LinkedHashMapBuilder;
import aiss.githubminer.utils.ProjectIterator;
import aiss.githubminer.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class GitHubAPIService {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    final int PER_PAGE;
    private final String GITHUB_API_URL;
    private final String TOKEN;
    private final RestTemplate restTemplate;
    private final Map<String,?> BODY;
    private final String GITMINER_URL;

    @Autowired
    public GitHubAPIService(@Value("${github.api.token:}") String TOKEN,
                            @Value("${github.api.url:}") String GITHUB_API_URL,
                            @Value("${github.default.perPage}") int PER_PAGE,
                            @Value("classpath:query.graphql") Resource resourceQuery,
                            @Value("${gitminer.api.url:}") String GITMINER_URL,
                            RestTemplate restTemplate) {
        this.TOKEN = TOKEN;
        this.restTemplate = restTemplate;
        this.GITHUB_API_URL = GITHUB_API_URL;
        this.PER_PAGE = PER_PAGE;
        this.GITMINER_URL = GITMINER_URL;
        try {
            this.BODY = Map.of("query", resourceQuery.getContentAsString(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to load GraphQL query from resources");
        }}


    public LinkedHashMap<String,?> sendGraphQLQuery(String extraQueries, Variables variables) throws HttpClientErrorException, UnknownHttpStatusCodeException {
        HttpHeaders headers = new HttpHeaders();
        if (TOKEN != null && !TOKEN.isEmpty()) {
            headers.set("Authorization", "Bearer " + TOKEN);
        }
        Map<String, Object> body = new HashMap<>(BODY);
        String query = body.get("query").toString().replace(
                "{{extraQueries}}",
                extraQueries);
        body.put("query", query);
        body.put("variables", variables);
        HttpEntity<Map<String, ?>> request = new HttpEntity<>(body, headers);
        ResponseEntity<LinkedHashMap<String, ?>> response = restTemplate.exchange(GITHUB_API_URL, HttpMethod.POST, request, new ParameterizedTypeReference<>() {});
        return response.getBody();
    }


    private void validateUserInput(String owner, String repo, int sinceCommits, int sinceIssues, int maxPages) {
        ValidationUtils.validateOwnerAndRepo(owner, repo);
        ValidationUtils.validateSinceCommits(sinceCommits);
        ValidationUtils.validateSinceIssues(sinceIssues);
        ValidationUtils.validateMaxPages(maxPages);
    }

    public Project getProject(String owner, String repo, int sinceCommits, int sinceIssues, int maxPages) {
        validateUserInput(owner, repo, sinceCommits, sinceIssues, maxPages);
        LocalDateTime now = LocalDateTime.now();
        String resultCommits = now.minusDays(sinceCommits).format(formatter);
        String resultIssues = now.minusDays(sinceIssues).format(formatter);
        int elements = PER_PAGE * maxPages;
        Variables variables = new Variables(owner, repo, resultCommits, resultIssues, true, 100,100,100, false, false);
        LinkedHashMapBuilder parameters = LinkedHashMapBuilder.of()
                .add("owner", owner)
                .add("repo", repo)
                .add("sinceCommits", sinceCommits)
                .add("sinceIssues", sinceIssues)
                .add("maxPages", maxPages);
        ProjectIterator projectIterator = new ProjectIterator(elements, variables, this, parameters);
        while (projectIterator.hasNext()) projectIterator.next();
        return ValidationUtils.validateObject(projectIterator.getProject());
    }

    public void sendProject(Project project) {
        ValidationUtils.validateObject(project);
        try {
            restTemplate.postForObject(GITMINER_URL, project, Void.class);
        } catch (RestClientResponseException e) {
            LinkedHashMapBuilder errorResponse = LinkedHashMapBuilder.of().add("error", "An error occurred while sending the project to GitMiner");
            throw new GitHubMinerException(e.getStatusCode(), (e.getResponseBodyAsString().isEmpty() ?
                    errorResponse : errorResponse.add("GitHubMinerResponse", e.getResponseBodyAs(Object.class)))
                    .add("project", project)
            );
        } catch (RuntimeException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, LinkedHashMapBuilder.of()
                    .add("error", "An error occurred while sending the project to GitMiner")
                    .add("project", project)
            );
        }
    }

}
