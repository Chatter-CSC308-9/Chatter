package main.boundaries.screens;

import main.boundaries.apis.interfaces.Navigator;
import main.controllers.DisplayUsernameController;
import main.controllers.LogoutController;

public class GraderAccount extends AbstractAccount implements Navigator {

    public GraderAccount(LogoutController logoutController, DisplayUsernameController displayUsernameController) {
        this.logoutController = logoutController;
        this.displayUsernameController = displayUsernameController;
        super.addController(this.logoutController);
        super.addController(this.displayUsernameController);
    }

}
