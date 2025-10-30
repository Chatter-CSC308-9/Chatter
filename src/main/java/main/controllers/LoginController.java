package main.controllers;

import main.adapters.CredentialsRepository;
import main.boundaries.shell_apis.hooks.ShellSetUserAPI;
import main.boundaries.shell_apis.interfaces.SetsUser;
import main.entities.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class LoginController extends Controller implements SetsUser {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private ShellSetUserAPI shellSetUserAPI;

    // not yet implemented
    public boolean verifyCredentials(String username, String password) throws IOException {
        CredentialsRepository credentialsRepository = new CredentialsRepository();
        Optional<UserCredentials> userCredentials = credentialsRepository.getUserCredentials(username);
        if (userCredentials.isPresent() && password.equals(userCredentials.get().plaintextPassword)) {
            setUser(userCredentials.get().userID);
            logger.debug("{}/{}", username, password);
            return true;
        } else if (userCredentials.isPresent()) {
            logger.debug("Incorrect password");
            return false;
        }
        logger.debug("User not found");
        return false;
    }

    private void setUser(int userID) {
        shellSetUserAPI.setUserID(userID);
    }

    @Override
    public void setUserSettingAPI(ShellSetUserAPI shellSetUserAPI) {
        this.shellSetUserAPI = shellSetUserAPI;
    }
}
