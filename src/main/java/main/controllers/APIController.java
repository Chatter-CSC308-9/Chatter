package main.controllers;

import main.boundaries.shell_apis.hooks.ShellGetUserAPI;
import main.boundaries.shell_apis.hooks.ShellSetUserAPI;
import main.boundaries.shell_apis.interfaces.NeedsUser;
import main.boundaries.shell_apis.interfaces.SetsUser;

public class APIController extends Controller implements ShellGetUserAPI, ShellSetUserAPI {

    private String currentUserID;

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
    public String getUserID() {
        return this.currentUserID;
    }

    @Override
    public void setUserID(String userID) {
        this.currentUserID = userID;
    }

}
