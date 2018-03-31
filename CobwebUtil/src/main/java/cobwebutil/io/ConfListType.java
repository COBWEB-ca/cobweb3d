package cobwebutil.io;

import java.lang.annotation.*;


/**
 * List field component type. Required because of type erasure.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfListType {

    Class<?> value();
}
