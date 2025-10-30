package main.controllers;

import main.boundaries.shell_apis.hooks.ShellSetUserAPI;
import main.boundaries.shell_apis.interfaces.SetsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements SetsUser {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private ShellSetUserAPI shellSetUserAPI;

    // not yet implemented
    public boolean verifyCredentials(String username, String password) {
        logger.debug("{}/{}", username, password);
        setUser(username);
        return true;
    }

    private void setUser(String userID) {
        shellSetUserAPI.setUserID(userID);
    }

    @Override
    public void setUserSettingAPI(ShellSetUserAPI shellSetUserAPI) {
        this.shellSetUserAPI = shellSetUserAPI;
    }
}
