package main.boundaries.shell_apis.interfaces;

import main.boundaries.shell_apis.hooks.ShellGetUserAPI;

public interface NeedsUser {
    void setGetUserAPI(ShellGetUserAPI shellGetUserAPI);
}
