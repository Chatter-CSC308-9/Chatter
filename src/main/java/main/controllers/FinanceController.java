package main.controllers;

import main.controllers.apis.hooks.GetUserAPI;
import main.controllers.apis.interfaces.NeedsUser;

public class FinanceController implements Controller, NeedsUser {

    private GetUserAPI getUserAPI;

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }

    public long getUserID() {
        return getUserAPI.getUserID();
    }
}
