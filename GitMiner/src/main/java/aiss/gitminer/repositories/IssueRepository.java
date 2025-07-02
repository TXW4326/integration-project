package aiss.gitminer.repositories;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Issue;
import aiss.gitminer.utils.ValidationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, String> {
    Page<Issue> findByProjectId(String projectId, Pageable pageable);

    Page<Issue> findByAuthor_Id(String authorId, Pageable pageable);

    Page<Issue> findByAuthor_IdAndState(String authorId, String state, Pageable pageable);

    Page<Issue> findByState(String state, Pageable pageable);

    default void issueExists(String issueId) {
        ValidationUtils.validateIssueId(issueId);
        if (!existsById(issueId)) throw new IssueNotFoundException();
    }
}
