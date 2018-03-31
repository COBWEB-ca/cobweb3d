package cobwebutil.io;

import java.lang.annotation.*;

/**
 * This field should not have its own configuration section, any child properties should be merged
 * with parent Has priority over {@link ConfXMLTag}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfSquishParent {

}
