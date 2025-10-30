package main.boundaries.taskbars;

import main.boundaries.shell_apis.interfaces.Navigator;
import main.boundaries.shell_apis.hooks.ShellNavigateAPI;

public class GraderTaskbar implements Navigator {

    private ShellNavigateAPI shellNavigateAPI;

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }
}
