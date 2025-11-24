package tests;

import main.controllers.ClaimUngradedProjectController;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClaimUngradedProjectControllerTest {
    @Test
    public void testGetTitle() {
        var claimUngradedProjectController = new ClaimUngradedProjectController();
        String title = claimUngradedProjectController.getTitle("D1E6E4E92");
        assertEquals("Pending and unclaimed", title);
    }

    @Test
    public void testGetAvailableProjectNames() {
        var claimUngradedProjectController = new ClaimUngradedProjectController();
        String[] output = claimUngradedProjectController.getAvailableProjectNames();
        assertArrayEquals(new String[]{"D1E6E4E92",}, output);
    }
}
