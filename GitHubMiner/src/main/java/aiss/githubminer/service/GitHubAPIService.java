package aiss.githubminer.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.time.format.DateTimeFormatter;

@Service
public class GitHubAPIService {
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    final int PER_PAGE;
    private final String GITHUB_API_URL;
    private final String TOKEN;
    private final RestTemplate restTemplate;

    @Autowired
    public GitHubAPIService(@Value("${github.api.token:}") String TOKEN,
                            @Value("${github.api.url:}") String GITHUB_API_URL,
                            @Value("${github.default.perPage}") int PER_PAGE,
                            RestTemplate restTemplate) {
        this.TOKEN = TOKEN;
        this.restTemplate = restTemplate;
        this.GITHUB_API_URL = GITHUB_API_URL;
        this.PER_PAGE = PER_PAGE;
    }

    public <T> T get(String url, Class<T> responseType, Object... uriVariables) throws HttpClientErrorException, UnknownHttpStatusCodeException {
        String fullUrl = GITHUB_API_URL + url;
        HttpHeaders headers = new HttpHeaders();
        if (TOKEN != null && !TOKEN.isEmpty()) {
            headers.set("Authorization", "Bearer " + TOKEN);
        }
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<T> response = restTemplate.exchange(fullUrl, HttpMethod.GET, request, responseType, uriVariables);
        return response.getBody();
    }

}
