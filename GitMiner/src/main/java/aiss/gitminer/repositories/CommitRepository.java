package aiss.gitminer.repositories;

import aiss.gitminer.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitRepository extends JpaRepository<Commit, String> {
}
