package main.controllers;

import main.boundaries.shell_apis.hooks.ShellGetUserAPI;
import main.boundaries.shell_apis.hooks.ShellSetUserAPI;
import main.boundaries.shell_apis.interfaces.NeedsUser;
import main.boundaries.shell_apis.interfaces.SetsUser;

public class APIController implements Controller, ShellGetUserAPI, ShellSetUserAPI {

    private long currentUserID;

    public void injectControllerAPIs(Controller controller) {
        if (controller instanceof NeedsUser) injectGetUserAPI(controller);
        if (controller instanceof SetsUser) injectSetUserAPI(controller);
    }

    private void injectGetUserAPI(Object controller) {
        if (controller instanceof NeedsUser userGetter) {
            userGetter.setGetUserAPI(this);
        }
    }

    private void injectSetUserAPI(Object controller) {
        if (controller instanceof SetsUser userSetter) {
            userSetter.setUserSettingAPI(this);
        }
    }

    @Override
    public long getUserID() {
        return this.currentUserID;
    }

    @Override
    public void setUserID(long userID) {
        this.currentUserID = userID;
    }

}
