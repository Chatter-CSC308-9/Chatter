package main.controllers;

import main.adapters.UserHydratinator;
import main.controllers.apis.hooks.GetUserAPI;
import main.controllers.apis.interfaces.NeedsUser;
import main.entities.User;

public class DisplayUsernameController implements Controller, NeedsUser {

    private GetUserAPI getUserAPI;

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }

    public String getUsername() {
        var userHydratinator = new UserHydratinator();
        User user = userHydratinator.getUser(this.getUserAPI.getUserID());
        return user.username;
    }
}
