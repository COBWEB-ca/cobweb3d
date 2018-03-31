package cobwebutil.io;

import java.lang.annotation.*;

/**
 * Parser-friendly name to use for field when serializing it
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfXMLTag {

    String value();
}
