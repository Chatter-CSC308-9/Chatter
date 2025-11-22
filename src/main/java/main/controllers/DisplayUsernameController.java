package main.controllers;

import main.adapters.ProjectHydratinator;
import main.adapters.UserHydratinator;
import main.controllers.apis.hooks.GetUserAPI;
import main.controllers.apis.interfaces.NeedsUser;
import main.entities.Project;
import main.entities.User;

public class DisplayUsernameController implements Controller, NeedsUser {

    private GetUserAPI getUserAPI;

    UserHydratinator userHydratinator = new UserHydratinator();
    ProjectHydratinator projectHydratinator = new ProjectHydratinator();

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }

    public String getUsername() {
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());
        return user.username;
    }

    public String getEmail() {
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());
        return user.email;
    }

    public int[] getNumOfEachProjectType() {
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());
        int[] nums = {user.projects.length, 0, 0};
        for (String proj : user.completedProjects) {
            Project p = projectHydratinator.getProject(proj);
            if (Boolean.TRUE.equals(p.graded)) {
                nums[2]++;
            }
            else {
                nums[1]++;
            }
        }
        return nums;
    }

    public int[] getNumOfEachProjectTypeGrader() {
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());
        return new int[]{user.projects.length, user.completedProjects.length};
    }
}
