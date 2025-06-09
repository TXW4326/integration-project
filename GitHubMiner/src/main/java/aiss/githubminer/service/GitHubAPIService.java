package aiss.githubminer.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubAPIService {

    private static final String GITHUB_API_URL = "https://api.github.com/";
    private static final String TOKEN = System.getenv("github.api.token");

    public static <T> T get(String url, Class<T> responseType, RestTemplate restTemplate) {
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
