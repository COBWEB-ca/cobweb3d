package cobweb3d.ui.exceptions;

/**
 * Exceptions that occur due to user input. The user should see the exception message through the UI.
 */
public class UserInputException extends RuntimeException {

    private static final long serialVersionUID = 1l;

    public UserInputException(String message) {
        super(message);
    }

    public UserInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
