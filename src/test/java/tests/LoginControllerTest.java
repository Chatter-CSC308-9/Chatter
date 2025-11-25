package tests;

import main.controllers.LoginController;
import main.controllers.apis.UserIdApiController;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class LoginControllerTest {
    @Test
    public void testCorrectLearnerLogin() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("perry", "j");
        credentials.put("doof", "passwordinator");
        Optional<Boolean> expected = Optional.of(false);
        assertTrue(testLoginResult(credentials, expected));
    }

    @Test
    public void testCorrectGraderLogin() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("ferb", "iamferb");
        Optional<Boolean> expected = Optional.of(true);
        assertTrue(testLoginResult(credentials, expected));
    }

    @Test
    public void testIncorrectLogins() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("ferb", "iamnotferb");
        credentials.put("idontexist", "nopass");
        Optional<Boolean> expected = Optional.empty();
        assertTrue(testLoginResult(credentials, expected));
    }

    private boolean testLoginResult(Map<String, String> credentials, Optional<Boolean> expected) {
        var loginController = initializeLoginController();
        for (String username : credentials.keySet()) {
            Optional<Boolean> result = loginController.verifyCredentials(username, credentials.get(username));
            if (!expected.equals(result)) return false;
        }
        return true;
    }

    private LoginController initializeLoginController() {
        var setUserApi = new UserIdApiController();
        var loginController = new LoginController();
        loginController.setUserSettingAPI(setUserApi);
        return loginController;
    }
}
