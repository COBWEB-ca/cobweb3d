package cobweb3d.ui.application;

import cobweb3d.impl.SimulationConfig;
import cobweb3d.ui.AppContext;
import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class CobwebApplicationSwingBase extends JFrame implements AppContext {
    public static final String WINDOW_TITLE = "COBWEB 3D";

    public static final String CONFIG_FILE_EXTENSION = ".xml";
    public static final String TEMPORARY_FILE_EXTENSION = ".cwtemp";

    public static final String INITIAL_OR_NEW_INPUT_FILE_NAME = "initial_or_new_input_(reserved)" + CONFIG_FILE_EXTENSION;
    public static final String DEFAULT_DATA_FILE_NAME = "default_config";
    public static final String CURRENT_DATA_FILE_NAME = "current_data_(reserved)" + TEMPORARY_FILE_EXTENSION;

    public final Logger logger = Logger.getLogger("COBWEB3D");

    /**
     * Filename of current simulation config
     */
    protected String currentFile;

    public CobwebApplicationSwingBase() {
        super(WINDOW_TITLE);
        logInfo("Welcome to Cobweb 3D");
        logInfo("JVM Memory: " + Runtime.getRuntime().maxMemory() / 1024 + "KB");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitApplication();
            }
        });
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("64icon.png")));
        //ImageIO.read(.getResource("res/icon.png"));
        setSize(580, 650);
        setLocationRelativeTo(null);
    }

    @Override
    public void quitApplication() {
        Platform.exit();
        getContentPane().removeAll();
        dispose();
        System.exit(0);
    }

    @Override
    public File openFile(SimulationConfig config, boolean continuation) {
        File file = new File(config.fileName);
        if (file.exists()) {
            try {
                currentFile = file.getCanonicalPath();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        setTitle(WINDOW_TITLE + " - " + file.getName());
        return file;
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void logInfo(String string) {
        logger.info(string);
    }

    public void logWarn(String string) {
        logger.warning(string);
    }

    public void logErr(String string) {
        logger.severe(string);
    }
}
