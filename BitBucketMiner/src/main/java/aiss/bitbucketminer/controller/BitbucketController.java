package aiss.bitbucketminer.controller;

import aiss.bitbucketminer.models.Project;
import aiss.bitbucketminer.service.BitbucketProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/bitbucket")
@RequiredArgsConstructor
public class BitbucketController {

    private final BitbucketProjectService bitbucketService;

    @GetMapping("/{workspace}/{repoSlug}")
    public ResponseEntity<?> getRepository(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        if (nCommits < 1 || nIssues < 1 || maxPages < 1) {
            throw new IllegalArgumentException("Los parámetros deben ser mayores que cero.");
        }

        try {
            Project project = bitbucketService.fetchProject(workspace, repoSlug, nCommits, nIssues, maxPages);
            return ResponseEntity.ok(Collections.singletonList(project));
        } catch (HttpClientErrorException | ResourceAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error accediendo a Bitbucket: " + ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error inesperado: " + e.getMessage()));
        }
    }

    @PostMapping("/{workspace}/{repoSlug}")
    public ResponseEntity<?> sendData(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        if (nCommits < 1 || nIssues < 1 || maxPages < 1) {
            throw new IllegalArgumentException("Los parámetros deben ser mayores que cero.");
        }

        try {
            Project project = bitbucketService.fetchProject(workspace, repoSlug, nCommits, nIssues, maxPages);
            String message = "Proyecto enviado correctamente!";
            return ResponseEntity.ok(String.format("%s: %s", message, project));
        } catch (HttpClientErrorException | ResourceAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error accediendo a Bitbucket: " + ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error inesperado: " + e.getMessage()));
        }
    }
}
