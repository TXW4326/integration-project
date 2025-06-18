package aiss.githubminer.service;


import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Commit;
import aiss.githubminer.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class CommitService {

    private final GitHubAPIService gitHubAPIService;

    @Autowired
    public CommitService(GitHubAPIService gitHubAPIService) {
        this.gitHubAPIService = gitHubAPIService;
    }

    private Commit[] handleCommitApiCall(String owner, String repo, String resultCommits, int page, int maxPages, int sinceCommits) {
        try {
            return gitHubAPIService.get("repos/{owner}/{repo}/commits?since={sinceCommits}&page={page}", Commit[].class, owner, repo, resultCommits, page);
        } catch (HttpClientErrorException e) {
            Map<String,?> parameters = Map.of(
                    "owner", owner,
                    "repo", repo,
                    "sinceCommits", sinceCommits,
                    "maxPages", maxPages,
                    "page", page
            );
            switch (e.getStatusCode().value()) {
                case 400: throw new GitHubMinerException(HttpStatus.BAD_REQUEST, Map.of(
                        "error", "Invalid request parameters for commits",
                        "parameters", parameters));
                case 404: throw new GitHubMinerException(HttpStatus.NOT_FOUND,Map.of(
                        "error", "No commits found for the given parameters",
                        "parameters",parameters ));
                case 409: throw new GitHubMinerException(HttpStatus.CONFLICT, Map.of(
                        "error", "Conflict occurred while fetching commits",
                        "parameters", parameters));
                case 500: throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(
                        "error", "Internal server error while fetching commits",
                        "parameters", parameters));
                default: throw new GitHubMinerException(e.getStatusCode(), Map.of(
                        "error", "An error occurred while fetching commits",
                        "parameters", parameters));
            }
        } catch (UnknownHttpStatusCodeException e) {
            throw new GitHubMinerException(e.getStatusCode(), Map.of(
                    "error", "An unknown error occurred while fetching commits",
                    "parameters", Map.of(
                            "owner", owner,
                            "repo", repo,
                            "sinceCommits", sinceCommits,
                            "maxPages", maxPages,
                            "page", page)
            ));
        } catch (RuntimeException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(
                    "error", "An error occurred while fetching commits",
                    "parameters", Map.of(
                            "owner", owner,
                            "repo", repo,
                            "sinceCommits", sinceCommits,
                            "maxPages", maxPages,
                            "page", page)
            ));
        }
    }

    List<Commit> getCommitsInternal(String owner, String repo, int sinceCommits, int maxPages) {
        LocalDateTime now = LocalDateTime.now();
        String resultCommits = now.minusDays(sinceCommits).format(GitHubAPIService.formatter);
        List<Commit> commits = new ArrayList<>();
        for (int page = 1; page <= maxPages; page++) {
            Commit[] commitArray = handleCommitApiCall(owner, repo, resultCommits, page, maxPages, sinceCommits);
            commits.addAll(Arrays.asList(commitArray));
            if (commitArray.length < gitHubAPIService.PER_PAGE) break;
        }
        return commits;
    }

    // Method implemented in case it is needed to get commits without getting the project first
    public List<Commit> getCommits(String owner, String repo, int sinceCommits, int maxPages) {
        userInputValidation(owner, repo, sinceCommits, maxPages);
        return getCommitsInternal(owner, repo, sinceCommits, maxPages);
    }

    private static void userInputValidation(String owner, String repo, int sinceCommits, int maxPages) {
        ValidationUtils.validateOwnerAndRepo(owner, repo);
        ValidationUtils.validateSinceCommits(sinceCommits);
        ValidationUtils.validateMaxPages(maxPages);
    }
}
