package main.boundaries.taskbars;

import main.boundaries.Navigator;
import main.boundaries.ShellAPI;

public class GraderTaskbar implements Navigator {

    private ShellAPI shellAPI;

    @Override
    public void setShellAPI(ShellAPI shellAPI) {
        this.shellAPI = shellAPI;
    }
}
