package main.controllers;

import main.controllers.apis.hooks.SetUserAPI;
import main.controllers.apis.interfaces.SetsUser;

public class LogoutController implements Controller, SetsUser {

    private SetUserAPI setUserAPI;

    public void logout() {
        setUserAPI.setUserID(0);
    }

    @Override
    public void setUserSettingAPI(SetUserAPI setUserAPI) {
        this.setUserAPI = setUserAPI;
    }
}
