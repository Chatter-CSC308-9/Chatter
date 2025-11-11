package main.controllers;

import main.FileProcessingException;
import main.adapters.ProjectHydratinator;
import main.adapters.UserHydratinator;
import main.controllers.apis.hooks.GetUserAPI;
import main.controllers.apis.interfaces.NeedsUser;
import main.entities.Project;
import main.entities.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class SubmitProjectController implements Controller, NeedsUser {

    GetUserAPI getUserAPI;

    private static final String PROJECTS_DIRECTORY = "server/projects/";
    private static final String TITLE_FILE_NAME = "/title.txt";

    // submit project
    public void submitProject(String projectFolder) {
        // get project and user
        ProjectHydratinator projectHydratinator = new ProjectHydratinator();
        Project proj = projectHydratinator.getProject(projectFolder);
        UserHydratinator userHydratinator = new UserHydratinator();
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());

        // update project
        proj.submitted = true;

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

    // return names of folders holding projects for user
    public String[] getCompletedProjectNames() {

        UserHydratinator userHydratinator = new UserHydratinator();
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());

        return user.completedProjects;
    }

    // return title of project
    public String getTitle(String projectName) {
        String title = "hello world";
        try(BufferedReader br = new BufferedReader(new FileReader(PROJECTS_DIRECTORY + projectName + TITLE_FILE_NAME))) {
            title = br.readLine();
        } catch (IOException e) {
            throw new FileProcessingException("Failed to get title", e);
        }

        return title;
    }

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }
}
