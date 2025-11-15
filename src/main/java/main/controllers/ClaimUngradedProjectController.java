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
import java.util.ArrayList;
import java.util.Arrays;

public class ClaimUngradedProjectController implements Controller, NeedsUser {

    private static final Logger logger = LoggerFactory.getLogger(ClaimUngradedProjectController.class);

    ProjectHydratinator projectHydratinator = new ProjectHydratinator();
    UserHydratinator userHydratinator = new UserHydratinator();

    GetUserAPI getUserAPI;

    // claim project
    public Boolean claimProject(String projDir) {
        Project proj = projectHydratinator.getProject(projDir);

        // project already claimed
        if (Boolean.TRUE.equals(proj.claimed)) {
            return false;
        }

        // update project
        proj.claimed = true;
        proj.graderID = this.getUserAPI.getUserID();
        projectHydratinator.setProject(proj);

        // update grader's information
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());
        user.projects = Arrays.copyOf(user.projects, user.projects.length + 1);
        user.projects[user.projects.length - 1] = projDir;
        userHydratinator.setUser(user);

        return true;
    }

    // get title of project
    public String getTitle(String projDir) {
        return projectHydratinator.getProject(projDir).projectTitle;
    }

    // return names of submitted and unclaimed projects
    public String[] getAvailableProjectNames() {

        ArrayList<String> projNames = new ArrayList<>();

        File dir = new File("server/projects_list");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                String projDir = f.getName().substring(0, f.getName().length() - 5);
                if (!projDir.isEmpty()) { // weird bug that creates ".json" in projects_list when creating new project
                    Project p = projectHydratinator.getProject(projDir);
                    if (Boolean.TRUE.equals(p.submitted) && p.graderID == 0) {
                        projNames.add(projDir);
                    }
                }
            }
        } else {
            logger.info("problem accessing projects_list");
        }

        return projNames.toArray(new String[projNames.size()]);
    }

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }
}
