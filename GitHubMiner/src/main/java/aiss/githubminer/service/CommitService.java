package aiss.githubminer.service;


import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.Commit;
import aiss.githubminer.utils.LinkedHashMapBuilder;
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

@Service
public class CommitService {

    private final GitHubAPIService gitHubAPIService;

    @Autowired
    public CommitService(GitHubAPIService gitHubAPIService) {
        this.gitHubAPIService = gitHubAPIService;
    }

    private Commit[] handleCommitApiCall(String owner, String repo, String resultCommits, int page, int maxPages, int sinceCommits) {
        try {
            return gitHubAPIService.get(
                    "repos/{owner}/{repo}/commits?since={sinceCommits}&page={page}",
                    Commit[].class,
                    owner, repo, resultCommits, page
            );
        } catch (HttpClientErrorException e) {
            LinkedHashMapBuilder parameters = LinkedHashMapBuilder.of()
                    .add("owner", owner)
                    .add("repo", repo)
                    .add("sinceCommits", sinceCommits)
                    .add("maxPages", maxPages)
                    .add("page", page);

            switch (e.getStatusCode().value()) {
                case 400:
                    throw new GitHubMinerException(HttpStatus.BAD_REQUEST, LinkedHashMapBuilder.of()
                        .add("error", "Invalid request parameters for commits")
                        .add("parameters", parameters)
                    );
                case 404:
                    throw new GitHubMinerException(HttpStatus.NOT_FOUND, LinkedHashMapBuilder.of()
                        .add("error", "No commits found for the given parameters")
                        .add("parameters", parameters)
                    );
                case 409:
                    throw new GitHubMinerException(HttpStatus.CONFLICT, LinkedHashMapBuilder.of()
                        .add("error", "Conflict occurred while fetching commits")
                        .add("parameters", parameters)
                    );
                case 500:
                    throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, LinkedHashMapBuilder.of()
                        .add("error", "Internal server error while fetching commits")
                        .add("parameters", parameters)
                    );
                default:
                    throw new GitHubMinerException(e.getStatusCode(), LinkedHashMapBuilder.of()
                        .add("error", "An error occurred while fetching commits")
                        .add("parameters", parameters)
                    );
            }
        } catch (UnknownHttpStatusCodeException e) {
            LinkedHashMapBuilder parameters = LinkedHashMapBuilder.of()
                    .add("owner", owner)
                    .add("repo", repo)
                    .add("sinceCommits", sinceCommits)
                    .add("maxPages", maxPages)
                    .add("page", page);

            throw new GitHubMinerException(e.getStatusCode(), LinkedHashMapBuilder.of()
                    .add("error", "An unknown error occurred while fetching commits")
                    .add("parameters", parameters)
            );
        } catch (RuntimeException e) {
            LinkedHashMapBuilder parameters = LinkedHashMapBuilder.of()
                    .add("owner", owner)
                    .add("repo", repo)
                    .add("sinceCommits", sinceCommits)
                    .add("maxPages", maxPages)
                    .add("page", page);

            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, LinkedHashMapBuilder.of()
                .add("error", "An error occurred while fetching commits")
                .add("parameters", parameters)
            );
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
