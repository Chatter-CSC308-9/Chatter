package main.controllers.apis.interfaces;

import main.controllers.apis.hooks.GetUserAPI;

public interface NeedsUser {
    void setGetUserAPI(GetUserAPI getUserAPI);
}
