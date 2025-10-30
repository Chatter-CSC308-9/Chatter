package main.controllers;

import main.FileProcessingException;
import main.boundaries.screens.CurrentEdit;
import main.boundaries.shell_apis.hooks.ShellGetUserAPI;
import main.boundaries.shell_apis.interfaces.NeedsUser;
import main.entities.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


// NOTE: PATHS TO TITLE AND WORK FILES ARE HARDCODED EXCEPT NAME OF PROJECT FOLDER


public class EditProjectController extends Controller implements NeedsUser {

    private static final Logger logger = LoggerFactory.getLogger(EditProjectController.class);

    CurrentEdit currentEditBoundary;
    String projectFolder;
    Project project;
    ShellGetUserAPI shellGetUserAPI;

    private static final String PROJECTS_DIRECTORY = "server/projects/";

    public void setCurrentEditBoundary(CurrentEdit ceb) {
        this.currentEditBoundary = ceb;
    }

    public void setProject(String projectFolder) {
        this.projectFolder = projectFolder;
    }

    public void editProject(String projectFolder) {
        this.projectFolder = projectFolder;
        // use json to load information into project entity
    }

    // return title of project
    public String getTitle() {
        String title = "hello world";
        try(BufferedReader br = new BufferedReader(new FileReader(PROJECTS_DIRECTORY + projectFolder + "/title.txt"))) {
            title = br.readLine();
        } catch (IOException e) {
            throw new FileProcessingException("Failed to get title", e);
        }

        return title;
    }

    // return text of project
    public String getWork() {
        String everything = "hello world";
        try(BufferedReader br = new BufferedReader(new FileReader(PROJECTS_DIRECTORY + projectFolder + "/work.txt"))) {
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
        try (FileWriter writer = new FileWriter(PROJECTS_DIRECTORY + projectFolder + "/title.txt")) {
            writer.write(title);
        } catch (IOException e) {
            logger.error("error", e);
        }

        // write to work file
        try (FileWriter writer = new FileWriter(PROJECTS_DIRECTORY + projectFolder + "/work.txt")) {
            writer.write(work);
        } catch (IOException e) {
            logger.error("error", e);
        }
    }

    // return names of folders holding projects (currently assumed to be all files in server)
    public List<String> getProjectNames() {
        ArrayList<String> projectNames = new ArrayList<>();

        File parentDir = new File("server/projects"); // should this be changed to projectsDirectory?
        File[] dirs = parentDir.listFiles();

        if (dirs != null) {
            for (File dir : dirs) {
                projectNames.add(dir.getName());
            }
        }

        return projectNames;
    }

    @Override
    public void setGetUserAPI(ShellGetUserAPI shellGetUserAPI) {
        this.shellGetUserAPI = shellGetUserAPI;
    }
}
