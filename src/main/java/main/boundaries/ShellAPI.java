package main.boundaries;

// ShellAPI allows Boundaries to access certain Shell functionalities
// without having full access to the shell.
public interface ShellAPI {
    void setTaskbar(String taskbarName);
    void setContent(String screenName);
}
