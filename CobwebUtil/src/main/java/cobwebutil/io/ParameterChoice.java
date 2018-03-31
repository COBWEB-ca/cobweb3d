package cobwebutil.io;

import java.io.Serializable;


/**
 * Parameter value that is one of multiple choices available
 */
public interface ParameterChoice extends Serializable {

    /**
     * Unique Identifier for this choice. This identifier is used to serialize and de-serialize the
     * parameter.
     *
     * @return identifier
     */
    String getIdentifier();

    /**
     * User-friendly name of the choice.
     *
     * @return user-friendly name
     */
    String getName();
}
