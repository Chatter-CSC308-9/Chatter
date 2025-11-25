package tests;

import main.adapters.UserHydratinator;
import main.entities.User;
import org.junit.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class UserHydratinatorTest {

    @Test
    public void getUserSuccess() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TestingServerUtil.clearTestServer();
        UserHydratinator uh = getTestingRepository();
        User user = new User();
        user.userID = 1L;
        uh.setUser(user);
        assertEquals(user, uh.getUser(1L));
        TestingServerUtil.clearTestServer();
    }

    @Test
    public void getUserFail() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TestingServerUtil.clearTestServer();
        UserHydratinator uh = getTestingRepository();
        assertNull(uh.getUser(1L));
    }

    @Test
    public void setUser() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TestingServerUtil.clearTestServer();
        UserHydratinator uh = getTestingRepository();
        User user = new User();
        user.userID = 2863311530L;
        uh.setUser(user);
        Path path = Path.of("test-server\\users\\aaaaaaaa.json");
        assertTrue(Files.exists(path));
        TestingServerUtil.clearTestServer();
    }

    private UserHydratinator getTestingRepository() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<UserHydratinator> testingConstructor = UserHydratinator.class.getDeclaredConstructor(boolean.class);
        testingConstructor.setAccessible(true);
        return testingConstructor.newInstance(true);
    }
}
