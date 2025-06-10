package aiss.githubminer.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubAPIService {

    private static final String GITHUB_API_URL = "https://api.github.com/";

    @Value("${github.api.token:}")
    private String TOKEN;

    @Autowired
    RestTemplate restTemplate;

    public <T> T get(String url, Class<T> responseType) {
        String fullUrl = GITHUB_API_URL + url;
        HttpHeaders headers = new HttpHeaders();
        if (TOKEN != null && !TOKEN.isEmpty()) {
            headers.set("Authorization", "Bearer " + TOKEN);
        }
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<T> response = restTemplate.exchange(fullUrl, HttpMethod.GET, request, responseType);
        //TODO: Handle errors properly
        return response.getBody();
    }
}
