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
import java.util.Optional;
import java.util.stream.Stream;

public class UserHydratinator {

    private static final Logger logger = LoggerFactory.getLogger(UserHydratinator.class);

    public Optional<User> getUser(long userID) {

        String jsonText = null;
        Stream<String> lines = null;
        try {
            lines = Files.lines(Paths.get("server/users/" + Long.toHexString(userID) + ".json"));
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

        return Optional.of(user);
    }

    public void setUser(User user) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File("server/users/" + Long.toHexString(user.userID) + ".json"), user);
        } catch (IOException e) {
            logger.error("error", e);
        }
    }
}
