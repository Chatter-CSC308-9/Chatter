package tests;

import main.adapters.CredentialsRepository;
import main.entities.UserCredentials;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class CredentialsRepositoryTest {

    @Test
    public void getAllUserCredentialsTest() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // create testing CredentialsRepository
        Constructor<CredentialsRepository> testingConstructor = CredentialsRepository.class.getDeclaredConstructor(boolean.class);
        testingConstructor.setAccessible(true);
        var credentialsRepository = testingConstructor.newInstance(true);

        // 0 loops, no users
        TestingServerUtil.clearTestServer();
        List<UserCredentials> expected = new ArrayList<>();
        List<UserCredentials> result = credentialsRepository.getAllUserCredentials();
        assertEquals(expected, result);

        // 1 loop, 1 user
        expected.add(new UserCredentials("a", "b", 1L, false));
        for (UserCredentials userCredentials : expected) {
            credentialsRepository.addUserCredential(userCredentials);
        }
        result = credentialsRepository.getAllUserCredentials();
        assertEquals(new HashSet<>(expected), new HashSet<>(result));

        // 2 loops, 2 users
        TestingServerUtil.clearTestServer();
        expected.add(new UserCredentials("b", "a", 2L, true));
        for (UserCredentials userCredentials : expected) {
            credentialsRepository.addUserCredential(userCredentials);
        }
        result = credentialsRepository.getAllUserCredentials();
        assertEquals(new HashSet<>(expected), new HashSet<>(result));

        // 50 loops, 50 users
        TestingServerUtil.clearTestServer();
        for (int i = 0; i < 48; i++) {
            expected.add(new UserCredentials("a", "a", (long) i, false));
        }
        for (UserCredentials userCredentials : expected) {
            credentialsRepository.addUserCredential(userCredentials);
        }
        result = credentialsRepository.getAllUserCredentials();
        assertEquals(new HashSet<>(expected), new HashSet<>(result));

        TestingServerUtil.clearTestServer();
    }
}
