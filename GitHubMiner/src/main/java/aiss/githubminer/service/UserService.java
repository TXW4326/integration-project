package aiss.githubminer.service;


import aiss.githubminer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final GitHubAPIService gitHubAPIService;

    @Autowired
    public UserService(GitHubAPIService gitHubAPIService) {
        this.gitHubAPIService = gitHubAPIService;
    }

    public User getUser(String username) {
        return gitHubAPIService.get("users/{username}", User.class, username);
    }
}
