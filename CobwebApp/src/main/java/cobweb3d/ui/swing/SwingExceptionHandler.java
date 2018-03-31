package cobweb3d.ui.swing;

import cobweb3d.ui.exceptions.LoggingExceptionHandler;
import cobweb3d.ui.exceptions.UserInputException;

import javax.swing.*;

/**
 * Shows pop-ups for UserInputException and uncaught exceptions.
 */
public class SwingExceptionHandler extends LoggingExceptionHandler {

    @Override
    protected void notificationUncaughtException(String errorText) {
        JOptionPane.showMessageDialog(null, "Oh no! You crashed COBWEB!\n" + errorText
                , "Unexpected Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void notificationUserInputException(UserInputException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
    }
}
