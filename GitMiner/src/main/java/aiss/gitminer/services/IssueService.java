package aiss.gitminer.services;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repositories.IssueRepository;
import aiss.gitminer.repositories.ProjectRepository;
import aiss.gitminer.repositories.UserRepository;
import aiss.gitminer.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    @Autowired
    private IssueService(
            IssueRepository issueRepository,
            ProjectRepository projectRepository,
            CommentService commentService,
            UserRepository userRepository
        ) {
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    List<Issue> findByProjectIdInternal(String projectId, Pageable pageIssues, Pageable pageComments) {
        return issueRepository.findByProjectId(projectId, pageIssues).stream().peek(issue -> {
            issue.setComments(commentService.findByIssueIdInternal(issue.getId(), pageComments));
        }).toList();
    }

    public List<Issue> findByProjectId(String projectId, Pageable pageIssues, Pageable pageComments) {
        projectRepository.projectExists(projectId);
        return findByProjectIdInternal(projectId, pageIssues, pageComments);
    }

    public Issue findIssueById(String issueId, Pageable pageComments) {
        ValidationUtils.validateIssueId(issueId);
        Issue issue = issueRepository.findById(issueId).orElseThrow(IssueNotFoundException::new);
        issue.setComments(commentService.findByIssueIdInternal(issueId, pageComments));
        return issue;
    }

    public List<Issue> findAll(Pageable pageIssues, Pageable pageComments) {
        return issueRepository.findAll(pageIssues).stream().peek(issue -> {
            issue.setComments(commentService.findByIssueIdInternal(issue.getId(), pageComments));
        }).toList();
    }

    public List<Issue> findAllByAuthorId(String authorId, Pageable pageIssues, Pageable pageComments) {
        userRepository.userExists(authorId);
        return issueRepository.findByAuthor_Id(authorId, pageIssues).stream().peek(issue -> {
            issue.setComments(commentService.findByIssueIdInternal(issue.getId(), pageComments));
        }).toList();
    }

    public List<Issue> findAllByState(String state, Pageable pageIssues, Pageable pageComments) {
        ValidationUtils.validateIssueState(state);
        return issueRepository.findByState(state, pageIssues).stream().peek(issue -> {
            issue.setComments(commentService.findByIssueIdInternal(issue.getId(), pageComments));
        }).toList();
    }

    public List<Issue> findAllByAuthorIdAndState(String authorId, String state, Pageable pageIssues, Pageable pageComments) {
        userRepository.userExists(authorId);
        ValidationUtils.validateIssueState(state);
        return issueRepository.findByAuthor_IdAndState(authorId, state, pageIssues).stream().peek(issue -> {
            issue.setComments(commentService.findByIssueIdInternal(issue.getId(), pageComments));
        }).toList();
    }

}
