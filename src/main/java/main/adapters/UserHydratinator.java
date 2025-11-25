package main.adapters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class UserHydratinator {

    private static final Logger logger = LoggerFactory.getLogger(UserHydratinator.class);

    private static final String USERS_REPOSITORY = "server/users/";

    private final boolean isTesting;

    public UserHydratinator() {
        this(false);
    }

    private UserHydratinator(boolean isTesting) {
        this.isTesting = isTesting;
    }

    private String getUsersRepository() {
        return this.isTesting ? "test-" + USERS_REPOSITORY : USERS_REPOSITORY;
    }

    public User getUser(long userID) {

        String jsonText = null;
        Stream<String> lines = null;
        try {
            lines = Files.lines(Paths.get(getUsersRepository() + Long.toHexString(userID) + ".json"));
            jsonText = lines.reduce("", String::concat);
        }
        catch (IOException e) {
            logger.error("error retrieving user info", e);
        }
        finally {
            if (lines != null) {
                lines.close();
            }
        }
        ObjectMapper mapper = new ObjectMapper();

        User user = null;
        try {
            user = mapper.readValue(jsonText, User.class);
        } catch (JsonProcessingException e) {
            logger.error("error hydrating user entity", e);
        }

        return user;
    }

    public void setUser(User user) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File(getUsersRepository() + Long.toHexString(user.userID) + ".json"), user);
        } catch (IOException e) {
            logger.error("error", e);
        }
    }
}
