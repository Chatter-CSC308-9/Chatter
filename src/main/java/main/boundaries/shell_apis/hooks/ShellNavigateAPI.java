package main.boundaries.shell_apis.hooks;

// ShellAPI allows Boundaries to access certain Shell functionalities
// without having full access to the shell.
public interface ShellNavigateAPI {
    void setTaskbar(String taskbarName);
    void setContent(String screenName);
}
