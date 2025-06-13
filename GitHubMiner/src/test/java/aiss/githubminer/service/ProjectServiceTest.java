package aiss.githubminer.service;

import aiss.githubminer.model.Project;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    ProjectService projectService;


    @Test
    @DisplayName("Get project details for spring-projects/spring-framework")
    void getProject() throws JsonProcessingException {
        String owner = "spring-projects";
        String repo = "spring-framework";

        Project project = projectService.getProject(owner, repo, 5, 20, 2);

        assertNotNull(project);
        assertEquals("spring-framework", project.getName());
        assertEquals("https://github.com/spring-projects/spring-framework", project.getWeb_url());
        assertEquals(1148753, project.getId().intValue());

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        System.out.println(ow.writeValueAsString(project));
    }
}