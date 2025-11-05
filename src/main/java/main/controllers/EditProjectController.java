package main.controllers;

import main.FileProcessingException;
import main.adapters.UserHydratinator;
import main.controllers.apis.hooks.GetUserAPI;
import main.controllers.apis.interfaces.NeedsUser;
import main.boundaries.shell_apis.hooks.ShellGetUserAPI;
import main.boundaries.shell_apis.interfaces.NeedsUser;
import main.entities.Project;
import main.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;

public class EditProjectController implements Controller, NeedsUser {

    private static final Logger logger = LoggerFactory.getLogger(EditProjectController.class);

    CurrentEdit currentEditBoundary;
    String projectFolder;
    Project project;
    GetUserAPI getUserAPI;

    private static final String PROJECTS_DIRECTORY = "server/projects/";
    private static final String TITLE_FILE_NAME = "/title.txt";
    private static final String WORK_FILE_NAME = "/work.txt";

    public void setProject(String projectFolder) {
        this.projectFolder = projectFolder;
    }

    public void editProject(String projectFolder) {
        this.projectFolder = projectFolder;
        // use json to load information into project entity
    }

    // create new project and prepare it for editing
    public void createProject() {
        UserHydratinator userHydratinator = new UserHydratinator();
        Optional<User> userOptional = userHydratinator.getUser(this.shellGetUserAPI.getUserID());
        User user = null;

        if (!userOptional.isPresent()) {
            return;
        }

        user = userOptional.get();
        user.numProjects++;
        String newProjDirName = (Long.toHexString(user.userID) + (user.numProjects - 1)).toUpperCase();


        // create new project directory
        File newProjDir = new File(PROJECTS_DIRECTORY + newProjDirName);
        if (!newProjDir.exists() && newProjDir.mkdir()) {

            // create title.txt and work.txt and add empty strings
            makeFile(PROJECTS_DIRECTORY + newProjDirName + TITLE_FILE_NAME);
            makeFile(PROJECTS_DIRECTORY + newProjDirName + WORK_FILE_NAME);
            editProject(newProjDirName);
            saveWork("title here", "work here");

            user.projects = Arrays.copyOf(user.projects, user.numProjects);
            user.projects[user.projects.length - 1] = newProjDirName;
        }

        // update user file
        userHydratinator.setUser(user);
    }

    // create given files
    private void makeFile(String pathName) {
        try {
            File file = new File(pathName);
            if (!file.createNewFile()) {
                logger.error("failed to create file");
            }
        } catch (Exception e) {
            logger.error("error in making file", e);
        }
    }

    // return title of project
    public String getTitle() {
        String title = "hello world";
        try(BufferedReader br = new BufferedReader(new FileReader(PROJECTS_DIRECTORY + projectFolder + TITLE_FILE_NAME))) {
            title = br.readLine();
        } catch (IOException e) {
            throw new FileProcessingException("Failed to get title", e);
        }

        return title;
    }

    // return text of project
    public String getWork() {
        String everything = "";
        try(BufferedReader br = new BufferedReader(new FileReader(PROJECTS_DIRECTORY + projectFolder + WORK_FILE_NAME))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (IOException e) {
            throw new FileProcessingException("Failed to get work", e);
        }

        return everything;
    }

    // save title and work
    public void saveWork(String title, String work) {
        // write to title file
        try (FileWriter writer = new FileWriter(PROJECTS_DIRECTORY + projectFolder + TITLE_FILE_NAME)) {
            writer.write(title);
        } catch (IOException e) {
            logger.error("error in writing to title file", e);
        }

        // write to work file
        try (FileWriter writer = new FileWriter(PROJECTS_DIRECTORY + projectFolder + WORK_FILE_NAME)) {
            writer.write(work);
        } catch (IOException e) {
            logger.error("error in writing to work file", e);
        }
    }

    // return names of folders holding projects (currently assumed to be all files in server)
    public String[] getProjectNames() {

        UserHydratinator userHydratinator = new UserHydratinator();
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());

        if (user.isPresent()) {
            return user.get().projects;
        }

        return new String[]{""};
    }

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }

    public long getUserID() {
        return this.getUserAPI.getUserID();
    }
}
