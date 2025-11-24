package main.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.entities.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CredentialsRepository {

    private static final Logger logger = LoggerFactory.getLogger(CredentialsRepository.class);

    private static final String CREDENTIALS_PATH = "server/credentials.jsonl";

    private final boolean isTesting;

    public CredentialsRepository() {
        this(false);
    }

    private CredentialsRepository(boolean isTesting) {
        this.isTesting = isTesting;
    }

    public String getCredentialsPath() {
        return isTesting ? "test-" + CREDENTIALS_PATH : CREDENTIALS_PATH;
    }

    public Optional<UserCredentials> getUserCredentials(String username) {
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedReader reader = new BufferedReader(new FileReader(getCredentialsPath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JsonNode node = mapper.readTree(line);

                if (node.get("username").asText().equals(username)) {
                    String passwordPlaintext = node.get("passwordplaintext").asText();
                    long userID = node.get("userID").asLong();
                    boolean isGrader = node.get("isGrader").asBoolean();
                    return Optional.of(new UserCredentials(username, passwordPlaintext, userID, isGrader));
                }
            }
        } catch (IOException exception) {
            logger.error("error", exception);
        }
        return Optional.empty();
    }
    public List<UserCredentials> getAllUserCredentials() {
        List<UserCredentials> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedReader reader = new BufferedReader(new FileReader(getCredentialsPath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JsonNode node = mapper.readTree(line);

                String username = node.get("username").asText();
                String passwordPlaintext = node.get("passwordplaintext").asText();
                long userID = node.get("userID").asLong();
                boolean isGrader = node.get("isGrader").asBoolean();
                list.add(new UserCredentials(username, passwordPlaintext, userID, isGrader));

            }
        } catch (IOException exception) {
            logger.error("error", exception);
        }
        return list;
    }
    public void addUserCredential(UserCredentials userCredential) {
        var objectMapper = new ObjectMapper();
        try (FileWriter fw = new FileWriter(getCredentialsPath(), true)) {
            String json = objectMapper.writeValueAsString(userCredential);
            fw.write(json + System.lineSeparator());
        } catch (IOException e) {
            logger.error(String.valueOf(e));
        }
    }
}
