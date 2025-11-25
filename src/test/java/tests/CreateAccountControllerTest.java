package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import main.controllers.CreateAccountController;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static tests.TestingServerUtil.logger;

public class CreateAccountControllerTest {

    public void cleanup(int projects) throws IOException {
        Path credentialsPath = Paths.get("server/credentials.jsonl");
        Path usersDir = Paths.get("server/users");
        List<String> lines = Files.readAllLines(credentialsPath);
        String last1 = lines.getLast();
        String last2 = lines.get(lines.size() - projects);

        ObjectMapper mapper = new ObjectMapper();

        long id1 = mapper.readTree(last1).get("userID").asLong();
        long id2 = mapper.readTree(last2).get("userID").asLong();

        // Delete user JSON files
        Files.deleteIfExists(usersDir.resolve(Long.toHexString(id1).toUpperCase() + ".json"));
        Files.deleteIfExists(usersDir.resolve(Long.toHexString(id2).toUpperCase() + ".json"));

        if (lines.isEmpty()) {
            logger.info("Not enough lines to delete. Aborting.");
            return;
        }

        // Remove last 2 test l
        List<String> cleaned = lines.subList(0, lines.size() - projects);

        Files.write(credentialsPath, cleaned, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    @Test
    public void createUserTest() throws IOException {
        // Fresh controller each time to avoid cross-contamination
        CreateAccountController controller = new CreateAccountController();
        Optional<Long> nonGrader = controller.createAccount("u1", "u1@mail.com", "pw1", false);
        assertTrue(nonGrader.isEmpty());
        Optional<Long> grader = controller.createAccount("u2", "u2@mail.com", "pw2", true);
        assertTrue(grader.isPresent());
        assertNotEquals(nonGrader, grader);
        cleanup(2);

    }

    @Test
    public void checkOnboardingComplete_NoStripeAccount_ReturnsFalse() throws StripeException, IOException {
        var controller = new CreateAccountController();

        // create a grader so we get back the userId in the Optional
        var userIdOpt = controller.createAccount(
                "graderUser",
                "grader@example.com",
                "password123",
                true
        );
        assertTrue("Expected userId for grader", userIdOpt.isPresent());

        long userId = userIdOpt.get();

        // Newly created user has no stripeId, so onboarding is not complete
        assertFalse(controller.checkOnboardingComplete(userId));
        cleanup(1);
    }

}
