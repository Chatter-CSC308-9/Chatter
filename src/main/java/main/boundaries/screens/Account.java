package main.boundaries.screens;

import main.boundaries.apis.interfaces.Navigator;
import main.controllers.DisplayUsernameController;
import main.controllers.LogoutController;

public class Account extends AbstractAccount implements Navigator {

    public Account(LogoutController logoutController, DisplayUsernameController displayUsernameController) {
        this.logoutController = logoutController;
        this.displayUsernameController = displayUsernameController;
        super.addController(this.logoutController);
        super.addController(this.displayUsernameController);
    }

}
