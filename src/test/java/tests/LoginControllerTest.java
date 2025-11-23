package tests;

import main.controllers.LoginController;
import main.controllers.apis.UserIdApiController;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class LoginControllerTest {
    @Test
    public void testCorrectLearnerLogin() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("perry", "j");
        credentials.put("doof", "passwordinator");
        Optional<Boolean> expected = Optional.of(false);
        testLoginResult(credentials, expected);
    }

    @Test
    public void testCorrectGraderLogin() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("ferb", "iamferb");
        Optional<Boolean> expected = Optional.of(true);
        testLoginResult(credentials, expected);
    }

    @Test
    public void testIncorrectLogins() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("ferb", "iamnotferb");
        credentials.put("idontexist", "nopass");
        Optional<Boolean> expected = Optional.empty();
        testLoginResult(credentials, expected);
    }

    private void testLoginResult(Map<String, String> credentials, Optional<Boolean> expected) {
        var loginController = initializeLoginController();
        for (String username : credentials.keySet()) {
            Optional<Boolean> result = loginController.verifyCredentials(username, credentials.get(username));
            assertEquals(expected, result);
        }
    }

    private LoginController initializeLoginController() {
        var setUserApi = new UserIdApiController();
        var loginController = new LoginController();
        loginController.setUserSettingAPI(setUserApi);
        return loginController;
    }
}
