package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.controllers.SubmitProjectController;
import main.controllers.apis.UserIdApiController;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HexFormat;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
public class SubmitProjectControllerTest {

    @Test
    public void testSubmission() throws IOException {
        var controller = new  SubmitProjectController();
        var getUserApi = new UserIdApiController();

        Path toUser = Paths.get("server/users/D1E6E4E9.json");
        Path toProject = Paths.get("server/projects_list/D1E6E4E90.json");

        List<String> originalUser = Files.readAllLines(toUser);
        List<String> originalProject = Files.readAllLines(toProject);

        ObjectMapper mapper = new ObjectMapper();

        String line = originalProject.getLast();

        boolean before = mapper.readTree(line).get("submitted").asBoolean();

        getUserApi.setUserID(HexFormat.fromHexDigitsToLong("D1E6E4E9"));
        controller.setGetUserAPI(getUserApi);
        controller.submitProject("D1E6E4E90");

        List<String> lines1 = Files.readAllLines(toProject);
        String line1 = lines1.getLast();
        boolean after = mapper.readTree(line1).get("submitted").asBoolean();
        assertNotEquals(before, after);

        Files.writeString(toUser, String.join("\n", originalUser));
        Files.writeString(toProject, String.join("\n", originalProject));

    }

    @Test
    public void testCompletedSuccess() throws IOException {
        var controller = new  SubmitProjectController();
        var getUserApi = new UserIdApiController();

        Path toUser = Paths.get("server/users/D1E6E4E9.json");
        Path toProject = Paths.get("server/projects_list/D1E6E4E90.json");

        List<String> originalUser = Files.readAllLines(toUser);
        List<String> originalProject = Files.readAllLines(toProject);

        List<String> lines = Files.readAllLines(toUser);
        String before = lines.getLast();

        getUserApi.setUserID(HexFormat.fromHexDigitsToLong("D1E6E4E9"));
        controller.setGetUserAPI(getUserApi);
        controller.submitProject("D1E6E4E90");

        List<String> lines1 = Files.readAllLines(toUser);
        String after = lines1.getLast();
        assertNotEquals(before, after);

        Files.writeString(toUser, String.join("\n", originalUser));
        Files.writeString(toProject, String.join("\n", originalProject));
    }
}
