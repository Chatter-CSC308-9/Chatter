package tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

public class TestingServerUtil {
    private static final Logger logger = LoggerFactory.getLogger(TestingServerUtil.class);

    private static final String SERVER_DIRECTORY = "test-server";

    @SuppressWarnings("java:S7467") // prevent erroneous error naming sonar issue
    public static void clearTestServer() {
        try (FileWriter fw = new FileWriter(SERVER_DIRECTORY + "/credentials.jsonl", false)) {
            fw.write("");
        } catch (IOException e) {
            logger.error("Credentials not found", e);
        }
        clearDirectory(SERVER_DIRECTORY + "/projects");
        clearDirectory(SERVER_DIRECTORY + "/projects_list");
        clearDirectory(SERVER_DIRECTORY + "/users");
    }

    @SuppressWarnings("java:S7467") // prevent erroneous error naming sonar issue
    private static void clearDirectory(String directory) {
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths.sorted(Comparator.reverseOrder())
                    .filter(path -> !path.equals(Paths.get(directory)))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            logger.warn("Failed to delete: {}", path, e);
                        }
                    });
        } catch (IOException e) {
            logger.error("Failed to clear directory: {}", directory, e);
        }
    }
}
