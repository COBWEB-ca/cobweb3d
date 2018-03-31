package cobwebutil.io;

import java.lang.annotation.*;

/**
 * Format string for displaying Array and Map items Array use: String.format("%s %i",
 * fieldConfDisplayName, arrayIndex) Map use: String.format("%s thing", mapKey)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfDisplayFormat {

    String value();
}
