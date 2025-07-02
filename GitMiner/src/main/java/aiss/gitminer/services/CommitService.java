package aiss.gitminer.services;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repositories.CommitRepository;
import aiss.gitminer.repositories.ProjectRepository;
import aiss.gitminer.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitService {

    private final CommitRepository commitRepository;
    private final ProjectRepository projectRepository;


    @Autowired
    private CommitService(
            CommitRepository commitRepository,
            ProjectRepository projectRepository
    ) {
        this.commitRepository = commitRepository;
        this.projectRepository = projectRepository;
    }


    public Commit findById(String id) {
        ValidationUtils.validateCommitId(id);
        return commitRepository.findById(id).orElseThrow(CommitNotFoundException::new);
    }

    List<Commit> findByProjectIdInternal(String projectId, Pageable pageable) {
        projectRepository.existsById(projectId);
        return commitRepository.findByProjectId(projectId, pageable).getContent();
    }


    public List<Commit> findAll(Pageable pageable) {
        return commitRepository.findAll(pageable).getContent();
    }
}
