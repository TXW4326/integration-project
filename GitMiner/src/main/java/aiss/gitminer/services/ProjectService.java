package aiss.gitminer.services;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repositories.ProjectRepository;
import aiss.gitminer.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final IssueService issueService;
    private final CommitService commitService;

    @Autowired
    private ProjectService(
            ProjectRepository projectRepository,
            IssueService issueService,
            CommitService commitService
    ) {
        this.projectRepository = projectRepository;
        this.issueService = issueService;
        this.commitService = commitService;
    }


    public List<Project> findAllProjects(Pageable pageProjects, Pageable pageComments, Pageable pageCommits, Pageable pageIssue) {
        return projectRepository.findAll(pageProjects).stream()
                .peek(project -> {
                    project.setIssues(issueService.findByProjectIdInternal(project.getId(), pageIssue, pageComments));
                    project.setCommits(commitService.findByProjectIdInternal(project.getId(), pageCommits));
                }).toList();
    }

    public Project findProjectById(String projectId, Pageable pageIssues, Pageable pageComments, Pageable pageCommits) {
        ValidationUtils.validateProjectId(projectId);
        Project project = projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        project.setIssues(issueService.findByProjectIdInternal(project.getId(), pageIssues, pageComments));
        project.setCommits(commitService.findByProjectIdInternal(project.getId(), pageCommits));
        return project;
    }


    public Project save(@Valid Project project) {
        return projectRepository.save(project);
    }
}
