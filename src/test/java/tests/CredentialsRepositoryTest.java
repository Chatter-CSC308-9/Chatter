package tests;

import main.adapters.CredentialsRepository;
import main.entities.UserCredentials;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.*;

public class CredentialsRepositoryTest {

    @Test
    public void getAllUserCredentialsTest() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // create testing CredentialsRepository
        var credentialsRepository = getTestingRepository();

        // 0 loops, no users
        TestingServerUtil.clearTestServer();
        List<UserCredentials> expected = new ArrayList<>();
        List<UserCredentials> result = credentialsRepository.getAllUserCredentials();
        assertEquals(expected, result);

        // 1 loop, 1 user
        expected.add(generateRandomUserCredential());
        for (UserCredentials userCredentials : expected) {
            credentialsRepository.addUserCredential(userCredentials);
        }
        result = credentialsRepository.getAllUserCredentials();
        assertEquals(new HashSet<>(expected), new HashSet<>(result));

        // 2 loops, 2 users
        TestingServerUtil.clearTestServer();
        expected.add(generateRandomUserCredential());
        for (UserCredentials userCredentials : expected) {
            credentialsRepository.addUserCredential(userCredentials);
        }
        result = credentialsRepository.getAllUserCredentials();
        assertEquals(new HashSet<>(expected), new HashSet<>(result));

        // 50 loops, 50 users
        TestingServerUtil.clearTestServer();
        for (int i = 0; i < 48; i++) {
            expected.add(generateRandomUserCredential());
        }
        for (UserCredentials userCredentials : expected) {
            credentialsRepository.addUserCredential(userCredentials);
        }
        result = credentialsRepository.getAllUserCredentials();
        assertEquals(new HashSet<>(expected), new HashSet<>(result));

        TestingServerUtil.clearTestServer();
    }

    @Test
    public void getUserCredentialsTest() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        var credentialsRepository = getTestingRepository();
        UserCredentials credentialToFind = new UserCredentials("perry", "", 1L, false);
        String usernameToGet = credentialToFind.username;

        // No credentials exist
        setupTest();
        assertEquals(Optional.empty(), credentialsRepository.getUserCredentials(usernameToGet));

        // User is first credential
        setupTest(credentialsRepository, credentialToFind);
        assertEquals(Optional.of(credentialToFind),credentialsRepository.getUserCredentials(usernameToGet));

        // User is second credential
        setupTest(credentialsRepository, 1, credentialToFind);
        assertEquals(Optional.of(credentialToFind),credentialsRepository.getUserCredentials(usernameToGet));

        // User is in the middle of many credentials
        setupTest(credentialsRepository, 50, credentialToFind, 50);
        assertEquals(Optional.of(credentialToFind),credentialsRepository.getUserCredentials(usernameToGet));

        // User is second-to-last credential
        setupTest(credentialsRepository, 50, credentialToFind, 1);
        assertEquals(Optional.of(credentialToFind),credentialsRepository.getUserCredentials(usernameToGet));

        // User is last credential
        setupTest(credentialsRepository, 50, credentialToFind);
        assertEquals(Optional.of(credentialToFind),credentialsRepository.getUserCredentials(usernameToGet));

        TestingServerUtil.clearTestServer();
    }

    private void setupTest(CredentialsRepository credentialsRepository, int before, UserCredentials target, int after) {
        TestingServerUtil.clearTestServer();
        addRandomCredentials(credentialsRepository, before);
        if (target != null) {
            credentialsRepository.addUserCredential(target);
        }
        addRandomCredentials(credentialsRepository, after);
    }

    private void setupTest(CredentialsRepository credentialsRepository, UserCredentials target) {
        setupTest(credentialsRepository, 0, target, 0);
    }

    private void setupTest(CredentialsRepository credentialsRepository, int before, UserCredentials target) {
        setupTest(credentialsRepository, before, target, 0);
    }

    private void setupTest() {
        TestingServerUtil.clearTestServer();
    }

    private void addRandomCredentials(CredentialsRepository credentialsRepository, int count) {
        for (int i = 0; i < count; i++) {
            credentialsRepository.addUserCredential(generateRandomUserCredential());
        }
    }

    private CredentialsRepository getTestingRepository() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<CredentialsRepository> testingConstructor = CredentialsRepository.class.getDeclaredConstructor(boolean.class);
        testingConstructor.setAccessible(true);
        return testingConstructor.newInstance(true);
    }

    private UserCredentials generateRandomUserCredential() {
        var random = new Random();
        String username = Double.toString(Math.random());
        String password = Double.toString(Math.random());
        long userId = random.nextLong();
        boolean isGrader = Math.random() < 0.5;
        return new UserCredentials(username, password, userId, isGrader);
    }

}
