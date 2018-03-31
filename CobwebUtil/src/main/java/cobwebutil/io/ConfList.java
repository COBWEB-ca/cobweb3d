package cobwebutil.io;

import java.lang.annotation.*;


/**
 * Name pattern to use for index when saving/loading lists
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfList {

    String[] indexName();

    boolean startAtOne();
}
