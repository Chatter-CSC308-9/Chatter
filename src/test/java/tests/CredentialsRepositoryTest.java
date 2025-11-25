package tests;

import main.adapters.CredentialsRepository;
import main.entities.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.*;

public class CredentialsRepositoryTest {

    private CredentialsRepository credentialsRepository;

    @Before
    public void setUp() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        credentialsRepository = getTestingRepository();
        TestingServerUtil.clearTestServer();
    }

    @After
    public void tearDown() {
        TestingServerUtil.clearTestServer();
    }

    // getAllUserCredentials tests

    @Test
    public void getAllUserCredentials_withNoUsers_returnsEmptyList() {
        List<UserCredentials> expected = new ArrayList<>();
        List<UserCredentials> result = credentialsRepository.getAllUserCredentials();
        assertEquals(expected, result);
    }

    @Test
    public void getAllUserCredentials_withOneUser_returnsOneUser() {
        List<UserCredentials> expected = new ArrayList<>();
        expected.add(generateRandomUserCredential());

        for (UserCredentials userCredentials : expected) {
            credentialsRepository.addUserCredential(userCredentials);
        }

        List<UserCredentials> result = credentialsRepository.getAllUserCredentials();
        assertEquals(new HashSet<>(expected), new HashSet<>(result));
    }

    @Test
    public void getAllUserCredentials_withTwoUsers_returnsTwoUsers() {
        List<UserCredentials> expected = new ArrayList<>();
        expected.add(generateRandomUserCredential());
        expected.add(generateRandomUserCredential());

        for (UserCredentials userCredentials : expected) {
            credentialsRepository.addUserCredential(userCredentials);
        }

        List<UserCredentials> result = credentialsRepository.getAllUserCredentials();
        assertEquals(new HashSet<>(expected), new HashSet<>(result));
    }

    @Test
    public void getAllUserCredentials_withFiftyUsers_returnsFiftyUsers() {
        List<UserCredentials> expected = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            expected.add(generateRandomUserCredential());
        }

        for (UserCredentials userCredentials : expected) {
            credentialsRepository.addUserCredential(userCredentials);
        }

        List<UserCredentials> result = credentialsRepository.getAllUserCredentials();
        assertEquals(new HashSet<>(expected), new HashSet<>(result));
    }

    // getUserCredentials tests

    @Test
    public void getUserCredentials_whenNoCredentialsExist_returnsEmpty() {
        UserCredentials credentialToFind = new UserCredentials("perry", "", 1L, false);
        String usernameToGet = credentialToFind.username;

        assertEquals(Optional.empty(), credentialsRepository.getUserCredentials(usernameToGet));
    }

    @Test
    public void getUserCredentials_whenUserIsFirstCredential_returnsUser() {
        UserCredentials credentialToFind = new UserCredentials("perry", "", 1L, false);
        String usernameToGet = credentialToFind.username;

        setupTest(credentialsRepository, credentialToFind);
        assertEquals(Optional.of(credentialToFind), credentialsRepository.getUserCredentials(usernameToGet));
    }

    @Test
    public void getUserCredentials_whenUserIsSecondCredential_returnsUser() {
        UserCredentials credentialToFind = new UserCredentials("perry", "", 1L, false);
        String usernameToGet = credentialToFind.username;

        setupTest(credentialsRepository, 1, credentialToFind);
        assertEquals(Optional.of(credentialToFind), credentialsRepository.getUserCredentials(usernameToGet));
    }

    @Test
    public void getUserCredentials_whenUserIsInMiddleOfManyCredentials_returnsUser() {
        UserCredentials credentialToFind = new UserCredentials("perry", "", 1L, false);
        String usernameToGet = credentialToFind.username;

        setupTest(credentialsRepository, 50, credentialToFind, 50);
        assertEquals(Optional.of(credentialToFind), credentialsRepository.getUserCredentials(usernameToGet));
    }

    @Test
    public void getUserCredentials_whenUserIsSecondToLastCredential_returnsUser() {
        UserCredentials credentialToFind = new UserCredentials("perry", "", 1L, false);
        String usernameToGet = credentialToFind.username;

        setupTest(credentialsRepository, 50, credentialToFind, 1);
        assertEquals(Optional.of(credentialToFind), credentialsRepository.getUserCredentials(usernameToGet));
    }

    @Test
    public void getUserCredentials_whenUserIsLastCredential_returnsUser() {
        UserCredentials credentialToFind = new UserCredentials("perry", "", 1L, false);
        String usernameToGet = credentialToFind.username;

        setupTest(credentialsRepository, 50, credentialToFind);
        assertEquals(Optional.of(credentialToFind), credentialsRepository.getUserCredentials(usernameToGet));
    }

    // Helper methods

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