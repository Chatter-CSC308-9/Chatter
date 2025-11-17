package main.controllers;

import main.adapters.CredentialsRepository;
import main.adapters.UserHydratinator;
import main.entities.User;
import main.entities.UserCredentials;

import java.util.List;
import java.util.Random;

public class CreateAccountController implements Controller {

    @SuppressWarnings("java:S2245") // Non-security randomness for uid generation
    private static final Random random = new Random();

    // returns true if async steps required, false otherwise
    public boolean createAccount(String username, String email, String password, boolean isGrader) {
        String userId = generateUserId();
        while (!verifyValidUserId(userId)) userId = generateUserId();
        var user = new User();
        user.username = username;
        user.userID = Long.parseLong(userId,16);
        user.passwordplaintext = password;
        user.isGrader = isGrader;
        user.email = email;
        user.emailIsHidden = false;
        user.projects = new String[0];
        user.completedProjects = new String[0];
        user.numProjects = 0;
        (new UserHydratinator()).setUser(user);
        var userCredentials = new UserCredentials(username, password, Long.parseLong(userId,16), isGrader);
        (new CredentialsRepository()).addUserCredential(userCredentials);

        return isGrader;
    }

    private String generateUserId() {
        long userId = 0;
        for (int i = 0; i < 8; i++) {
            userId = userId * 16 + random.nextInt(16);
        }
        StringBuilder userIdAsString = new StringBuilder(Long.toHexString(userId));
        while (userIdAsString.length()<8) {
            userIdAsString.insert(0, "0");
        }
        return userIdAsString.toString();
    }
    private boolean verifyValidUserId(String userId) {
        long userIdAsLong = Long.parseLong(userId,16);
        List<UserCredentials> userCredentials = (new CredentialsRepository()).getAllUserCredentials();
        for (var userCredential : userCredentials) {
            if (userCredential.userID== userIdAsLong) return false;
        }
        return true;
    }
}
