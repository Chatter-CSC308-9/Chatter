package main.controllers;

import main.util.FileProcessingException;
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
    ProjectHydratinator projectHydratinator = new ProjectHydratinator();

    private static final String PROJECTS_DIRECTORY = "server/projects/";
    private static final String TITLE_FILE_NAME = "/title.txt";
    private static final String WORK_FILE_NAME = "/work.txt";

    public void setProject(String projectFolder) {
        this.projectFolder = projectFolder;
    }
    public String getProject() {
        return projectFolder;
    }

    public void editProject(String projectFolder) {
        this.projectFolder = projectFolder;
        project = projectHydratinator.getProject(projectFolder);
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
            this.projectFolder = newProjDirName;

            // update user information
            user.projects = Arrays.copyOf(user.projects, user.projects.length + 1);
            user.projects[user.projects.length - 1] = newProjDirName;
            userHydratinator.setUser(user);

            // create project entity, create file to store its information, and save information
            try {
                Files.copy(Path.of("server/default_files/projectFolderName.json"), Path.of("server/projects_list/" + projectFolder + ".json"));
            } catch (IOException e) {
                logger.error("error creating new project json file", e);
            }

            this.project = projectHydratinator.getProject(newProjDirName);
            saveWork("title here", "work here");

            project.learnerID = user.userID;
            project.projectName = projectFolder;
            project.projectTitle = "title here";
            projectHydratinator.setProject(project);
        }
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
        // work accordingly with txt, mp3, and png files
        if (Boolean.TRUE.equals(isTXT(file.getPath()))) {
            try {
                Files.copy(file.toPath(), Path.of(PROJECTS_DIRECTORY + projectFolder + "/uploaded_work.txt"));
                project.hasUploadedTXT = true;
            } catch (IOException e) {
                logger.error("failed to upload txt", e);
            }
        }
        else if (Boolean.TRUE.equals(isMP3(file.getPath()))) {
            try {
                Files.copy(file.toPath(), Path.of(PROJECTS_DIRECTORY + projectFolder + "/uploaded_work.mp3"));
                project.hasUploadedMP3 = true;
            } catch (IOException e) {
                logger.error("failed to upload mp3", e);
            }
        }
        else if (Boolean.TRUE.equals(isPNG(file.getPath()))) {
            try {
                Files.copy(file.toPath(), Path.of(PROJECTS_DIRECTORY + projectFolder + "/uploaded_work.png"));
                project.hasUploadedPNG = true;
            } catch (IOException e) {
                logger.error("failed to upload png", e);
            }
        }
        else {
            logger.info("file is not valid");
        }

        // update project
        projectHydratinator.setProject(project);
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

    // check if project has uploaded .txt file
    public Boolean hasUploadedTXT() {
        return project.hasUploadedTXT;
    }

    // check if project has uploaded .mp3 file
    public Boolean hasUploadedMP3() {
        return project.hasUploadedMP3;
    }

    // check if project has uploaded .png file
    public Boolean hasUploadedPNG() {
        return project.hasUploadedPNG;
    }

    // delete uploaded txt file
    public void deleteUploadedTXT() {
        try {
            Files.deleteIfExists(Path.of(PROJECTS_DIRECTORY + projectFolder + "/uploaded_work.txt"));
        } catch (IOException e) {
            logger.error("error deleting uploaded .txt file", e);
        }
        project.hasUploadedTXT = false;
        projectHydratinator.setProject(project);
    }

    // delete uploaded mp3 file
    public void deleteUploadedMP3() {
        try {
            Files.deleteIfExists(Path.of(PROJECTS_DIRECTORY + projectFolder + "/uploaded_work.mp3"));
        } catch (IOException e) {
            logger.error("error deleting uploaded .mp3 file", e);
        }
        project.hasUploadedMP3 = false;
        projectHydratinator.setProject(project);
    }
    // delete uploaded png file
    public void deleteUploadedPNG() {
        try {
            Files.deleteIfExists(Path.of(PROJECTS_DIRECTORY + projectFolder + "/uploaded_work.png"));
        } catch (IOException e) {
            logger.error("error deleting uploaded .png file", e);
        }
        project.hasUploadedPNG = false;
        projectHydratinator.setProject(project);
    }

    // delete project
    public void deleteProject() {
        logger.info("delete project");

        // delete from user profile
        UserHydratinator userHydratinator = new UserHydratinator();
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());

        String[] updatedProjects = new String[user.projects.length - 1];
        int found = 0;
        for (int i = 0; i < updatedProjects.length; i++) {
            if (user.projects[i].equals(projectFolder)) {
                found = 1;
                continue;
            }
            updatedProjects[i] = user.projects[i + found];
        }
        user.projects = updatedProjects;
        userHydratinator.setUser(user);

        // delete folder
        if (Boolean.TRUE.equals(project.hasUploadedTXT)) {
            deleteUploadedTXT();
        }
        if (Boolean.TRUE.equals(project.hasUploadedMP3)) {
            deleteUploadedMP3();
        }
        if (Boolean.TRUE.equals(project.hasUploadedPNG)) {
            deleteUploadedPNG();
        }
        try {
            Files.deleteIfExists(Path.of(PROJECTS_DIRECTORY + projectFolder + TITLE_FILE_NAME));
            Files.deleteIfExists(Path.of(PROJECTS_DIRECTORY + projectFolder + WORK_FILE_NAME));
            Files.delete(Path.of(PROJECTS_DIRECTORY + projectFolder));
        } catch (IOException e) {
            logger.error("error deleting project folder", e);
        }

        // delete json
        try {
            Files.deleteIfExists(Path.of("server/projects_list/" + projectFolder + ".json"));
        } catch (IOException e) {
            logger.error("error deleting project json file", e);
        }
    }

    // save title and work
    public void saveWork(String title, String work) {
        // write to title file
        try (FileWriter writer = new FileWriter(PROJECTS_DIRECTORY + projectFolder + TITLE_FILE_NAME)) {
            writer.write(title);
            project.projectTitle = title;
            projectHydratinator.setProject(project);
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

        userHydratinator.setUser(user);

        return user.projects;
    }

    // submit project
    public void submitProject(SubmitProjectController spc) {
        spc.submitProject(projectFolder);
    }

    public void submitAIProject(SubmitProjectController spc, Runnable onSucceeded,
                                java.util.function.Consumer<Throwable> onFailed) {
        logger.debug("AI method called");
        spc.submitProjectToAI(projectFolder,onSucceeded,onFailed);
    }

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }

    public long getUserID() {
        return this.getUserAPI.getUserID();
    }
}
