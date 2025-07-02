package aiss.gitminer.repositories;

import aiss.gitminer.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    Page<Comment> findByIssueId(String issueId, Pageable pageable);

    Page<Comment> findByAuthor_Id(String authorId, Pageable pageable);

}
