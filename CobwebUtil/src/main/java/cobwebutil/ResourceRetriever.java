package cobwebutil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * Utility class that allows transparent reading of files from
 * the current working directory or from the classpath.
 *
 * @author Pepijn Van Eeckhoudt
 */
public class ResourceRetriever {
    public static URL getResource(final String fileName) throws IOException {
        // Try to load resource from jar
        URL url = ClassLoader.getSystemResource(fileName);
        // If not found in jar, then load from disk
        if (url == null) {
            return new URL("file", "localhost", fileName);
        } else {
            return url;
        }
    }

    public static InputStream getResourceAsStream(final String fileName) throws IOException {
        // Try to load resource from jar
        InputStream stream = ClassLoader.getSystemResourceAsStream(fileName);
        // If not found in jar, then load from disk
        if (stream == null) {
            return new FileInputStream(fileName);
        } else {
            return stream;
        }
    }

    public static String getResourceAsString(final String fileName) throws IOException {
        try (InputStream in = getResourceAsStream(fileName)) {
            return new Scanner(in, "UTF-8").useDelimiter("\\A").next();
        }
    }
}
