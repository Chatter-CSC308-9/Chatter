package main.controllers;

import main.boundaries.shell_apis.hooks.ShellSetUserAPI;
import main.boundaries.shell_apis.interfaces.SetsUser;

public class LogoutController implements Controller, SetsUser {

    private ShellSetUserAPI shellSetUserAPI;

    public void logout() {
        shellSetUserAPI.setUserID(0);
    }

    @Override
    public void setUserSettingAPI(ShellSetUserAPI shellSetUserAPI) {
        this.shellSetUserAPI = shellSetUserAPI;
    }
}
