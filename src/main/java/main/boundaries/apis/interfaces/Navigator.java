package main.boundaries.apis.interfaces;

import main.boundaries.apis.hooks.ShellNavigateAPI;

// Boundaries must be able to set a ShellAPI to be able to use ShellAPI to navigate
public interface Navigator {
    void setNavigateAPI(ShellNavigateAPI shellNavigateAPI);
    default void onShow() {}
}
