package cobwebutil.io;

import java.lang.annotation.*;


/**
 * Saves value class when serializing/deserializing
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfSaveInstanceClass {

}
