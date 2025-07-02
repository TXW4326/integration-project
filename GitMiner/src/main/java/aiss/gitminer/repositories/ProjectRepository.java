package aiss.gitminer.repositories;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.utils.ValidationUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    default void projectExists(String projectId) {
        ValidationUtils.validateProjectId(projectId);
        if (!existsById(projectId)) throw new ProjectNotFoundException();
    }
}
