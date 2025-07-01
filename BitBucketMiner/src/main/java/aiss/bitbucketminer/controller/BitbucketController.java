package aiss.bitbucketminer.controller;

import aiss.bitbucketminer.models.Project;
import aiss.bitbucketminer.service.BitbucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/bitbucket")
public class BitbucketController {

    @Autowired
    private BitbucketService bitbucketService;

    @GetMapping("/{workspace}/{repoSlug}")
    public ResponseEntity<?> getRepository(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages
    ) {

        System.out.println("Workspace: " + workspace);
        System.out.println("RepoSlug: " + repoSlug);
        System.out.println("nCommits: " + nCommits);
        System.out.println("nIssues: " + nIssues);
        Project project = bitbucketService.fetchData(
                workspace,repoSlug,nCommits,nIssues,
                maxPages,false);
        return ResponseEntity.ok(Collections.singletonList(
                project));
    }

    @PostMapping("/{workspace}/{repoSlug}")
    public ResponseEntity<?> sendData(
            @PathVariable String workspace,
            @PathVariable String repoSlug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        Project project = bitbucketService.fetchData(
                workspace, repoSlug,nCommits,nIssues,
                maxPages,true);
        String message = "Proyecto enviado correctamente!";
        return ResponseEntity.ok(String.format(
                "%s: %s",
                message, project));
    }
}
