package main.controllers;

import main.adapters.CredentialsRepository;
import main.controllers.apis.hooks.SetUserAPI;
import main.controllers.apis.interfaces.SetsUser;
import main.entities.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LoginController implements Controller, SetsUser {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private SetUserAPI setUserAPI;

    // verifies credentials and returns user type
    // null return means unverified, true means isGrader, false means learner
    public Optional<Boolean> verifyCredentials(String username, String password) {
        CredentialsRepository credentialsRepository = new CredentialsRepository();
        Optional<UserCredentials> userCredentials = credentialsRepository.getUserCredentials(username);
        if (userCredentials.isPresent() && password.equals(userCredentials.get().plaintextPassword)) {
            setUser(userCredentials.get().userID);
            logger.debug("{}/{}", username, password);
            return Optional.of(userCredentials.get().isGrader);
        } else if (userCredentials.isPresent()) {
            logger.debug("Incorrect password");
            return Optional.empty();
        }
        logger.debug("User not found");
        return Optional.empty();
    }

    private void setUser(long userID) {
        setUserAPI.setUserID(userID);
    }

    @Override
    public void setUserSettingAPI(SetUserAPI setUserAPI) {
        this.setUserAPI = setUserAPI;
    }
}
