package main.controllers;

import main.adapters.ProjectHydratinator;
import main.adapters.UserHydratinator;
import main.controllers.apis.hooks.GetUserAPI;
import main.controllers.apis.interfaces.NeedsUser;
import main.entities.Project;
import main.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class DownloadGradedProjectController implements Controller, NeedsUser {

    private static final Logger logger = LoggerFactory.getLogger(DownloadGradedProjectController.class);

    GetUserAPI getUserAPI;

    UserHydratinator userHydratinator = new UserHydratinator();
    ProjectHydratinator projectHydratinator = new ProjectHydratinator();

    // return names of graded projects
    public String[] getGradedProjectNames() {
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());

        ArrayList<String> projNames = new ArrayList<>();
        for (String projName : user.completedProjects) {
            Project proj = projectHydratinator.getProject(projName);
            if (Boolean.TRUE.equals(proj.graded)) {
                projNames.add(projName);
            }
        }

        return projNames.toArray(new String[projNames.size()]);
    }

    // download file to given location
    public int downloadFeedback(String projDir, File dest) {
        try {
            Files.copy(Path.of("server/projects/" + projDir + "/graded.txt"), Path.of(dest.toPath() + "/graded.txt"));
            return 0;
        } catch (IOException e) {
            logger.error("error downloading project", e);
            return 1;
        }
    }
    public int downloadAIFeedback(String projDir, File dest) {
        try {
            Path source = Path.of("server", "projects", projDir, "AIFeedback.txt");
            Path target = dest.toPath().resolve("AIFeedback.txt");

            // Delete and replace if file already exists
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return 0;
        } catch (IOException e) {
            logger.error("Error downloading project", e);
            return 1;
        }
    }

    // get title of project
    public String getTitle(String projDir) {
        return projectHydratinator.getProject(projDir).projectTitle;
    }

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }
}
