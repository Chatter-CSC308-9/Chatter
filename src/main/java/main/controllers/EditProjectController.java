package main.controllers;

import main.FileProcessingException;
import main.adapters.ProjectHydratinator;
import main.adapters.UserHydratinator;
import main.controllers.apis.hooks.GetUserAPI;
import main.controllers.apis.interfaces.NeedsUser;
import main.entities.Project;
import main.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class EditProjectController implements Controller, NeedsUser {

    private static final Logger logger = LoggerFactory.getLogger(EditProjectController.class);

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
    }

    // create new project and prepare it for editing
    public void createProject() {
        UserHydratinator userHydratinator = new UserHydratinator();
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());

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

    // upload file
    public void uploadFile(File file) {
        ProjectHydratinator projectHydratinator = new ProjectHydratinator();
        Project proj = projectHydratinator.getProject(projectFolder);

        // work accordingly with txt, mp3, and png files
        if (Boolean.TRUE.equals(isTXT(file.getPath()))) {
            try {
                Files.copy(file.toPath(), Path.of(PROJECTS_DIRECTORY + projectFolder + "/uploaded_work.txt"));
                proj.hasUploadedTXT = true;
            } catch (IOException e) {
                logger.error("failed to upload txt", e);
            }
        }
        else if (Boolean.TRUE.equals(isMP3(file.getPath()))) {
            try {
                Files.copy(file.toPath(), Path.of(PROJECTS_DIRECTORY + projectFolder + "/uploaded_work.mp3"));
                proj.hasUploadedMP3 = true;
            } catch (IOException e) {
                logger.error("failed to upload mp3", e);
            }
        }
        else if (Boolean.TRUE.equals(isPNG(file.getPath()))) {
            try {
                Files.copy(file.toPath(), Path.of(PROJECTS_DIRECTORY + projectFolder + "/uploaded_work.png"));
                proj.hasUploadedPNG = true;
            } catch (IOException e) {
                logger.error("failed to upload png", e);
            }
        }
        else {
            logger.info("file is not a txt");
        }

        // update project
        projectHydratinator.setProject(proj);
    }

    // check if file is .txt
    private Boolean isTXT(String filePath) {
        String end = filePath.substring(filePath.length() - 4);
        return end.equals(".txt");
    }

    // check if file is .mp3
    private Boolean isMP3(String filePath) {
        String end = filePath.substring(filePath.length() - 4);
        return end.equals(".mp3");
    }

    // check if file is .png
    private Boolean isPNG(String filePath) {
        String end = filePath.substring(filePath.length() - 4);
        return end.equals(".png");
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

    // return names of folders holding projects for user
    public String[] getProjectNames() {

        UserHydratinator userHydratinator = new UserHydratinator();
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());

        return user.projects;
    }

    // submit project
    public void submitProject(SubmitProjectController spc) {
        spc.submitProject(projectFolder);
    }

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }

    public long getUserID() {
        return this.getUserAPI.getUserID();
    }
}
