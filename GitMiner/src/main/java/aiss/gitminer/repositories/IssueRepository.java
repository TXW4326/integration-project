package aiss.gitminer.repositories;

import aiss.gitminer.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, String> {
}
