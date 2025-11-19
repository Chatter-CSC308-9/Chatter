package main.controllers;

import main.util.FileProcessingException;
import main.adapters.ProjectHydratinator;
import main.adapters.UserHydratinator;
import main.controllers.apis.hooks.GetUserAPI;
import main.controllers.apis.interfaces.NeedsUser;
import main.entities.Project;
import main.entities.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class SubmitProjectController implements Controller, NeedsUser {

    GetUserAPI getUserAPI;

    ProjectHydratinator projectHydratinator = new ProjectHydratinator();
    UserHydratinator userHydratinator = new UserHydratinator();
    private static final Logger logger = Logger.getLogger(SubmitProjectController.class.getName());
    private static final String PROJECTS_DIRECTORY = "server/projects/";
    private static final String TITLE_FILE_NAME = "/title.txt";

    // submit project
    public void submitProject(String projectFolder) {
        // get project and user
        Project proj = projectHydratinator.getProject(projectFolder);
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
    public String[] getSubmittedUngradedProjectNames() {
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());

        ArrayList<String> projNames = new ArrayList<>();
        for (String projName : user.completedProjects) {
            Project proj = projectHydratinator.getProject(projName);
            if (Boolean.FALSE.equals(proj.graded)) {
                projNames.add(projName);
            }
        }

        return projNames.toArray(new String[projNames.size()]);
    }

    // return title of project
    public String getTitle(String projectName) {
        String title = "hello world";
        try (BufferedReader br = new BufferedReader(new FileReader(PROJECTS_DIRECTORY + projectName + TITLE_FILE_NAME))) {
            title = br.readLine();
        } catch (IOException e) {
            throw new FileProcessingException("Failed to get title", e);
        }

        return title;
    }

    //Submit Project to AI
    public void submitProjectToAI(String projectFolder) {
        logger.info("entered submitProjToAI");
        File inputFile = new File(PROJECTS_DIRECTORY, projectFolder + "/work.txt");
        String absoluteInputPath = inputFile.getAbsolutePath();
        logger.info(absoluteInputPath);
        File parentDir = inputFile.getParentFile();
        File feedbackFile = new File(parentDir, "AIFeedback.txt");
        String feedbackPath = feedbackFile.getAbsolutePath();
        logger.info(feedbackPath);
        try{
            String scriptPath = "server/AI/reader.py";
            List<String> command = Arrays.asList(
                    "python",
                    scriptPath,
                    "--model", "llama3.1",
                    "--prompt_file", absoluteInputPath,
                    "--output", feedbackPath
            );
            // Create the process builder
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true); // merge stderr into stdout

            // Start the process
            Process p = pb.start();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = r.readLine()) != null) {
                    logger.info(line);
                }
                logger.info("complete");
            }

        }catch (Exception _){
            logger.warning("Failed to submit project to AI");
        }



    }


    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }
}
