package aiss.githubminer.service;


import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.User;
import aiss.githubminer.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

@Service
public class UserService {

    private final GitHubAPIService gitHubAPIService;

    @Autowired
    public UserService(GitHubAPIService gitHubAPIService) {
        this.gitHubAPIService = gitHubAPIService;
    }

    User getUserInternal(String username) {
        try {
            return gitHubAPIService.get("users/{username}", User.class, username);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 404) throw new GitHubMinerException(HttpStatus.NOT_FOUND, "User not found: " + username);
            throw new GitHubMinerException(e.getStatusCode(), "An error occurred while fetching the user: " + username);
        } catch (UnknownHttpStatusCodeException e) {
            throw new GitHubMinerException(e.getStatusCode(), "An unknown error occurred while fetching the user: " + username);
        } catch (RuntimeException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching the user: " + username);
        }
    }

    public User getUser(String username) {
        ValidationUtils.validateUsername(username);
        return getUserInternal(username);
    }

}
