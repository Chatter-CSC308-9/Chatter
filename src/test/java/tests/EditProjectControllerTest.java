package tests;

import main.controllers.EditProjectController;
import main.controllers.apis.UserIdApiController;
import org.junit.Test;

import java.util.HexFormat;

import static org.junit.Assert.*;

public class EditProjectControllerTest {
    @Test
    public void testHasUploadedTXT() {
        var editProjectController = new EditProjectController();
        editProjectController.editProject("D1E6E4E90");
        Boolean output = editProjectController.hasUploadedTXT();
        assertEquals(false, output);
    }

    @Test
    public void testGetProjectNames() {
        var getUserApi = new UserIdApiController();
        getUserApi.setUserID(HexFormat.fromHexDigitsToLong("D1E6E4E9"));
        var editProjectController = new EditProjectController();
        editProjectController.setGetUserAPI(getUserApi);

        String[] projNames = editProjectController.getProjectNames();
        assertArrayEquals(new String[]{"D1E6E4E90"}, projNames);
    }
}
