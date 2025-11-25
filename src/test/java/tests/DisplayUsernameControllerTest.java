package tests;
import main.controllers.apis.UserIdApiController;
import org.junit.Test;
import main.controllers.DisplayUsernameController;

import static org.junit.Assert.assertEquals;


public class DisplayUsernameControllerTest {
    @Test
    public void testGetUsername(){
        UserIdApiController uiac = new UserIdApiController();
        uiac.setUserID(1942783793);
        DisplayUsernameController duc = new DisplayUsernameController();
        duc.setGetUserAPI(uiac);
        assertEquals("doof", duc.getUsername());
    }
}
