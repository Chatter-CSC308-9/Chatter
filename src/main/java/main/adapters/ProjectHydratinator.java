package main.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.entities.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ProjectHydratinator {

    private static final Logger logger = LoggerFactory.getLogger(ProjectHydratinator.class);

    public Project getProject(String projectName) {

        String jsonText = null;
        Stream<String> lines = null;
        try {
            lines = Files.lines(Paths.get("server/projects_list/" + projectName + ".json"));
            jsonText = lines.reduce("", String::concat);
        }
        catch (IOException e) {
            logger.error("error retrieving project info", e);
        }
        finally {
            if (lines != null) {
                lines.close();
            }
        }
        ObjectMapper mapper = new ObjectMapper();

        Project proj = null;
        try {
            proj = mapper.readValue(jsonText, Project.class);
        } catch (JsonProcessingException e) {
            logger.error("error hydrating project entity", e);
        }

        return proj;
    }

    public void setProject(Project proj) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File("server/projects_list/" + proj.projectName + ".json"), proj);
        } catch (IOException e) {
            logger.error("error", e);
        }
    }
}
