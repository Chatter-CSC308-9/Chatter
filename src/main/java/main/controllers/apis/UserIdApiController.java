package main.controllers.apis;

import main.controllers.Controller;
import main.controllers.apis.hooks.GetUserAPI;
import main.controllers.apis.hooks.SetUserAPI;
import main.controllers.apis.interfaces.NeedsUser;
import main.controllers.apis.interfaces.SetsUser;

public class UserIdApiController implements ApiController, GetUserAPI, SetUserAPI {

    private long currentUserID;

    @Override
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
