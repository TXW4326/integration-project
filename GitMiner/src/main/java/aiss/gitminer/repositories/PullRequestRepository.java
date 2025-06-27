package aiss.gitminer.repositories;

import aiss.gitminer.model.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepository extends JpaRepository<PullRequest, String> {
}
