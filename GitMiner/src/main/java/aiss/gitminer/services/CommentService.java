package aiss.gitminer.services;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repositories.CommentRepository;
import aiss.gitminer.repositories.IssueRepository;
import aiss.gitminer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    @Autowired
    private CommentService(
            CommentRepository commentRepository,
            IssueRepository issueRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    public Comment findById(String id) {
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

    List<Comment> findByIssueIdInternal(String issueId, Pageable pageable) {
        return commentRepository.findByIssueId(issueId, pageable).getContent();
    }

    public List<Comment> findByIssueId(String issueId, Pageable pageable) {
        issueRepository.issueExists(issueId);
        return commentRepository.findByIssueId(issueId, pageable).getContent();
    }

    public List<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable).getContent();
    }

    public List<Comment> findAllByAuthorId(String authorId, Pageable pageable) {
        userRepository.userExists(authorId);
        return commentRepository.findByAuthor_Id(authorId, pageable).getContent();
    }
}
