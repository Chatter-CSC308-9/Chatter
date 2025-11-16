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
import java.util.Arrays;

public class SubmitGradedProjectController implements Controller, NeedsUser {

    private static final Logger logger = LoggerFactory.getLogger(SubmitGradedProjectController.class);

    ProjectHydratinator projectHydratinator = new ProjectHydratinator();
    UserHydratinator userHydratinator = new UserHydratinator();

    GetUserAPI getUserAPI;


    // return names of folders holding projects for user
    public String[] getProjectNames() {
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());

        userHydratinator.setUser(user);

        return user.projects;
    }

    // get title of project
    public String getTitle(String projDir) {
        return projectHydratinator.getProject(projDir).projectTitle;
    }

    // see if project has uploaded .txt
    public Boolean hasUploadedTXT(String projDir) {
        Project project = projectHydratinator.getProject(projDir);
        return project.hasUploadedTXT;
    }

    // see if project has uploaded .mp3
    public Boolean hasUploadedMP3(String projDir) {
        Project project = projectHydratinator.getProject(projDir);
        return project.hasUploadedMP3;
    }

    // see if project has uploaded .png
    public Boolean hasUploadedPNG(String projDir) {
        Project project = projectHydratinator.getProject(projDir);
        return project.hasUploadedPNG;
    }

    // download file to given location
    public int download(String projDir, String fileName, File dest) {
        try {
            Files.copy(Path.of("server/projects/" + projDir + fileName), Path.of(dest.toPath() + fileName));
            return 0;
        } catch (IOException e) {
            logger.error("error downloading project", e);
            return 1;
        }
    }

    // submit graded project
    public void submitGradedProject(String projectFolder) {
        // get project and user
        Project proj = projectHydratinator.getProject(projectFolder);
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());

        // update project
        proj.graded = true;

        // update user
        user.completedProjects = Arrays.copyOf(user.completedProjects, user.completedProjects.length + 1);
        user.completedProjects[user.completedProjects.length - 1] = projectFolder;
        String[] projectsUpdated = new String[user.projects.length - 1];
        int found = 0;
        for (int i = 0; i < user.projects.length; i++) {
            if (user.projects[i].equals(projectFolder)) {
                found = -1;
                continue;
            }
            projectsUpdated[i + found] = user.projects[i];
        }
        user.projects = projectsUpdated;

        // update project and user files
        projectHydratinator.setProject(proj);
        userHydratinator.setUser(user);
    }

    // upload file
    public int uploadFile(File file, String projDir) {
        if (Boolean.TRUE.equals(isTXT(file.getPath()))) {
            try {
                Files.copy(file.toPath(), Path.of("server/projects/" + projDir + "/graded.txt"));
                return 0;
            } catch (IOException e) {
                logger.error("failed to upload graded file", e);
            }
        }
        else {
            logger.info("file is not a txt");
        }
        return 1;
    }

    // check if project is graded
    public Boolean isGraded(String projDir) {
        return projectHydratinator.getProject(projDir).graded;
    }

    // check if file is .txt
    private Boolean isTXT(String filePath) {
        String end = filePath.substring(filePath.length() - 4);
        return end.equals(".txt");
    }

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }
}
