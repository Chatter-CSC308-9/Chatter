package main.controllers;

import main.adapters.ProjectHydratinator;
import main.controllers.apis.hooks.GetUserAPI;
import main.controllers.apis.interfaces.NeedsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcceptPaymentController implements Controller, NeedsUser {

    private static final Logger logger = LoggerFactory.getLogger(AcceptPaymentController.class);

    GetUserAPI getUserAPI;

    public boolean pay() throws InterruptedException {

        logger.debug("started");
        Thread.sleep(3000);
        logger.debug("ended");
        return true;

    }

    public int getCostInCents(String projDir) {
        ProjectHydratinator projectHydratinator = new ProjectHydratinator();
        return projectHydratinator.getProject(projDir).getCostInCents();
    }

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }
}
