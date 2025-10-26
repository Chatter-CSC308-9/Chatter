package main.boundaries;

// Boundaries must be able to set a ShellAPI to be able to use ShellAPI to navigate
public interface Navigator {
    void setShellAPI(ShellAPI shellAPI);
    default void onShow() {};
}
