package cobweb3d.ui.swing.components.logstate;

import cobweb3d.SimulationRunner;
import cobweb3d.ThreadSimulationRunner;
import cobweb3d.ui.UpdatableUI;
import cobweb3d.ui.swing.config.logging.LogConfigEditor;
import cobweb3d.ui.util.FileDialogUtil;
import cobwebutil.swing.SimpleAction;
import cobwebutil.swing.jfx.JFXFileExtFilter;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LogStateJMenuItems implements UpdatableUI.UpdateableLoggingUI {

    public ThreadSimulationRunner simRunner;
    public Component parent;

    public JCheckBoxMenuItem toggleLogCheckbox;
    public JMenuItem autoSaveLogMenuItem;
    public JMenuItem saveLogMenuItem;
    public JMenuItem configureLogMenuItem;
    private Action setAutoSaveLogAct = new SimpleAction("AutoSave Log", e -> {
        pauseUI();
        // TODO: Add CSV support with enabled/disabled logs.
        String path = FileDialogUtil.saveFileJFX(SwingUtilities.getWindowAncestor(parent), "AutoSave Simulation Log", JFXFileExtFilter.EXCEL_XLSX_SLOWEST);
        if (path != null && !path.isEmpty()) startSimulationLog(path);
    });
    private Action saveLogAct = new SimpleAction("Save Current Log", e -> {
        pauseUI();
        // TODO: Add CSV support with enabled/disabled logs.
        String path = FileDialogUtil.saveFileJFX(SwingUtilities.getWindowAncestor(parent), "Save Simulation Log", JFXFileExtFilter.EXCEL_XLSX_SLOWEST);
        if (path != null && !path.isEmpty()) saveSimulationLog(path);
    });
    private Action toggleLogAct = new SimpleAction("Log Data", e -> {
        boolean selected = ((AbstractButton) e.getSource()).isSelected();
        if (simRunner != null) {
            if (selected) simRunner.enableLogging();
            else simRunner.disableLogging();
        }
    });
    private Action showLogConfig = new SimpleAction("Configure Log", e -> {
        LogConfigEditor.show(SwingUtilities.getWindowAncestor(parent), simRunner.getSimulation());
        simRunner.setLogConfig(simRunner.getSimulation().simulationConfig.logConfig);
    });

    public LogStateJMenuItems(SimulationRunner simulationRunner, Component parent) {
        this.parent = parent;
        if (simulationRunner instanceof ThreadSimulationRunner)
            this.simRunner = (ThreadSimulationRunner) simulationRunner;
        simulationRunner.addUIComponent(this);
        toggleLogCheckbox = new JCheckBoxMenuItem(toggleLogAct);
        configureLogMenuItem = new JMenuItem(showLogConfig);
        autoSaveLogMenuItem = new JMenuItem(setAutoSaveLogAct);
        saveLogMenuItem = new JMenuItem(saveLogAct);
        if (simRunner != null) {
            toggleLogCheckbox.setState(simRunner.isLogging());
            saveLogMenuItem.setEnabled(simRunner.isLogging());
        }
    }

    @Override
    public void onLogStarted() {
        toggleLogCheckbox.setState(true);
        saveLogMenuItem.setEnabled(true);
    }

    @Override
    public void onLogStopped() {
        toggleLogCheckbox.setState(false);
        saveLogMenuItem.setEnabled(false);
    }

    public void pauseUI() {
        if (simRunner != null) simRunner.stop();
    }

    /**
     * Allows the user to select the log file to write to.
     */
    protected void startSimulationLog(String path) {
        if (simRunner == null) return;
        File file = new File(path);
        if (!file.exists() || file.canWrite()) {
            simRunner.setLogManagerAutoSaveFile(file);
        } else {
            if (!file.canWrite()) {
                JOptionPane.showMessageDialog(
                        parent,
                        "Caution:  File \"" + path + "\" is NOT allowed to be written to.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Allows the user to select the log file to write to.
     */
    protected void saveSimulationLog(String path) {
        if (simRunner == null) return;
        File file = new File(path);
        if (!file.exists() || file.canWrite()) {
            simRunner.saveLogManager(file);
        } else {
            if (!file.canWrite()) {
                JOptionPane.showMessageDialog(
                        parent,
                        "Caution:  File \"" + path + "\" is NOT allowed to be written to.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
