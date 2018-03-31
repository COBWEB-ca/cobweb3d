package cobweb3d;

import cobweb3d.impl.SimulationConfig;
import cobweb3d.io.Cobweb3Serializer;
import cobweb3d.ui.application.CobwebApplication;
import cobweb3d.ui.exceptions.LoggingExceptionHandler;
import cobweb3d.ui.swing.SwingExceptionHandler;

import javax.swing.*;

/**
 * This class contains the main method to drive the application.
 *
 * @author Cobweb Team (Might want to specify)
 */
public class CobwebApplicationRunner {

    /**
     * The main function is found here for the application version of cobweb. It initializes the
     * simulation and settings using a settings file optionally defined by the user.
     * <p>
     * <p>Switches:
     * <p>
     * <p><p> --help <br>Prints the various flags that can be used to run the program: Syntax =
     * "cobweb2 [--help] [-hide] [-autorun finalstep] [-log LogFile.tsv] [[-open] SettingsFile.xml]"
     * <p>
     * <p> -hide <br>When the hide flag is used, the user interface does not initialize (visible is
     * set to false).  If visible is set to false, the User Interface Client will be set to a
     * NullDisplayApplication rather than a CobwebApplication.  Need to specify an input file to use
     * this switch.
     * <p>
     * <p> -open [must specify] <br>If not used, the default is CobwebApplication.INITIAL_OR_NEW_INPUT_FILE_NAME
     * + CobwebApplication.CONFIG_FILE_EXTENSION otherwise will be set to whatever the user
     * specifies.  The input file contains the initial conditions of the simulation (AgentTypeCount,
     * FoodTypeCount, etc.)
     * <p>
     * <p> -log [must specify] <br>Specify the name of the log file.
     * <p>
     * <p> -autorun [specify integer >= -1]
     */

    public static void main(String[] args) {
        // Create CobwebApplication and threads; not done earlier so that argument errors will result in quick exits.
        boolean isDebug = BuildConfig.DEBUG;//java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
        if (!isDebug) {
            LoggingExceptionHandler handler = new SwingExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(handler);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new CobwebApplication().openFile(loadDefaultSimulation(), false);
    }

    private static SimulationConfig loadDefaultSimulation() {
        SimulationConfig defaultConfig = Cobweb3Serializer.loadConfig(Cobweb3Serializer.class.getClassLoader().getResourceAsStream("default_config.xml"));
        defaultConfig.fileName = "Default Simulation";
        return defaultConfig;
    }
}
