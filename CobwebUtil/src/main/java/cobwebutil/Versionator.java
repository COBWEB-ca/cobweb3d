package cobwebutil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Versionator {

    private static String version = null;

    public static String getVersion() {
        if (version != null) {
            return version;
        }

        // Try JAR file first
        version = Versionator.class.getPackage().getImplementationVersion();

        if (version == null) {
            // Else look in the version text file
            try {
                ClassLoader loader = Versionator.class.getClassLoader();
                InputStream stream = loader.getResourceAsStream("git.properties");
                if (stream != null) {
                    Properties properties = new Properties();
                    properties.load(stream);
                    String description = properties.getProperty("git.commit.id.describe");

                    if (description != null) {
                        if (description.endsWith("-modified")) {
                            version = String.format("%1$s (%2$s) %3$s",
                                    description,
                                    properties.getProperty("git.branch"),
                                    properties.getProperty("git.build.time"));
                        } else {
                            version = String.format("%1$s %2$s",
                                    description,
                                    properties.getProperty("git.commit.time"));
                        }
                    }
                } else version = "0.1"; // TODO!

            } catch (IOException ex) {
                // nothing
            }
        }

        if (version == null) {
            version = "Unknown Version";
        }

        return version;
    }
}
