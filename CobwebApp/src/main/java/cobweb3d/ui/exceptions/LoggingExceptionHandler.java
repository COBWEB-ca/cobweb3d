/**
 *
 */
package cobweb3d.ui.exceptions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.*;

/**
 * Handles uncaught exceptions for COBWEB. Displays UserInputException messages to user.
 * Logs all other exceptions into text file and displays stack trace to user.
 * By default both types of information are displayed on System.err.
 * <p>
 * Override
 * {@link LoggingExceptionHandler#notificationUncaughtException(String)} and
 * {@link LoggingExceptionHandler#notificationUserInputException(UserInputException)}
 * to change this behaviour.
 */
public class LoggingExceptionHandler implements UncaughtExceptionHandler {

    private final Logger logger = Logger.getLogger("COBWEB2");
    /**
     * System new line character for log file
     */
    private final String newLine = System.getProperty("line.separator");
    /**
     * Error log file Handler
     */
    private Handler errorLogWriter;

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // If it's an error the user can correct, don't need to log it
        if (ex instanceof UserInputException) {
            logger.log(Level.INFO, "User exception", ex);
            notificationUserInputException((UserInputException) ex);
            return;
        }

        // TODO remove? jfreechart used to have a concurrent modification error. It seems to be gone
        for (int depth = 0; depth < 4; depth++) {
            if (depth >= ex.getStackTrace().length)
                break;
            if (ex.getStackTrace()[depth].getClassName().equals("org.jfree.data.xy.DefaultXYDataset"))
                return;
        }

        setupErrorLogFile();
        logger.log(Level.SEVERE, "Uncaught Exception in thread " + thread.getName(), ex);

        StringBuilder sb = new StringBuilder();

        sb.append("Exception in thread " + thread.getName() + newLine);
        exceptionToString(ex, sb);

        notificationUncaughtException(sb.toString());
    }

    protected void notificationUserInputException(UserInputException ex) {
        System.err.println("Error:");
        System.err.println(ex.getMessage());
    }

    protected void notificationUncaughtException(String errorText) {
        System.err.println("Unexpected error:");
        System.err.println(errorText);
    }

    /**
     * Formats exception for logging/display
     *
     * @param ex Exception
     * @param sb StringBuilder to write the text to
     */
    private void exceptionToString(Throwable ex, StringBuilder sb) {
        exceptionToString(ex, sb, "");
    }

    private void exceptionToString(Throwable ex, StringBuilder sb, String indent) {
        sb.append(ex.toString() + newLine);
        for (StackTraceElement s : ex.getStackTrace()) {
            if (s.getClassName().startsWith("java"))
                continue;
            sb.append(indent + "  at " + s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber() + ")" + newLine);
        }
        if (ex.getCause() != null) {
            sb.append(indent + "Caused by: " + newLine);
            exceptionToString(ex.getCause(), sb, indent + "  ");
        }
    }

    /**
     * Create file to log uncaught exceptions.
     */
    private void setupErrorLogFile() {
        if (errorLogWriter == null) {
            try {
                errorLogWriter = new StreamHandler(new FileOutputStream("cobweb_errors.log", true), new SimpleFormatter());
                errorLogWriter.setLevel(Level.WARNING);
                logger.addHandler(errorLogWriter);
            } catch (FileNotFoundException exi) {
                logger.log(Level.SEVERE, "Cannot open error log file!", exi);
            }
        }
    }
}
