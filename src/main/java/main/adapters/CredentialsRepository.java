package main.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.entities.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class CredentialsRepository {

    private static final Logger logger = LoggerFactory.getLogger(CredentialsRepository.class);

    public Optional<UserCredentials> getUserCredentials(String username) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedReader reader = new BufferedReader(new FileReader("server/credentials.jsonl"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JsonNode node = mapper.readTree(line);

                if (node.get("username").asText().equals(username)) {
                    String passwordPlaintext = node.get("passwordplaintext").asText();
                    String email = node.get("email").asText();
                    int userID = node.get("userID").asInt();
                    return Optional.of(new UserCredentials(username, passwordPlaintext, email, userID));
                }
            }
        } catch (IOException exception) {
            logger.error("error", exception);
        }
        return Optional.empty();
    }
}
