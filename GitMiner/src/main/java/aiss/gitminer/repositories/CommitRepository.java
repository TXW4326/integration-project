package aiss.gitminer.repositories;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.utils.ValidationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitRepository extends JpaRepository<Commit, String> {
    Page<Commit> findByProjectId(String projectId, Pageable pageable);

    default void commitExists(String commitId) {
        ValidationUtils.validateCommitId(commitId);
        if (!existsById(commitId)) throw new CommitNotFoundException();
    }

}
