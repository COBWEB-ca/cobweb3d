package cobweb3d.ui.application;

import cobweb3d.ThreadSimulationRunner;
import cobweb3d.impl.Simulation;
import cobweb3d.impl.SimulationConfig;
import cobweb3d.io.Cobweb3Serializer;
import cobweb3d.ui.UpdatableUI;
import cobweb3d.ui.exceptions.UserInputException;
import cobweb3d.ui.swing.config.SimulationConfigEditor;
import cobweb3d.ui.swing.dialogs.AboutDialog;
import cobweb3d.ui.swing.dialogs.CreditsDialog;
import cobweb3d.ui.util.FileDialogUtil;
import cobwebutil.FileUtils;
import cobwebutil.ResourceRetriever;
import cobwebutil.swing.SimpleAction;
import cobwebutil.swing.jfx.JFXFileExtFilter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class CobwebApplicationSwing extends CobwebApplicationSwingBase {
    public ThreadSimulationRunner simRunner;

    public CobwebApplicationSwing() {
        this(new ThreadSimulationRunner(new Simulation()));
    }

    public CobwebApplicationSwing(ThreadSimulationRunner simulationRunner) {
        simRunner = simulationRunner;
    }

    @Override
    public Component add(Component comp) {
        if (comp instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) comp);
        return super.add(comp);
    }

    @Override
    public Component add(String name, Component comp) {
        if (comp instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) comp);
        return super.add(name, comp);
    }

    @Override
    public Component add(Component comp, int index) {
        if (comp instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) comp);
        return super.add(comp, index);
    }

    @Override
    public void add(@NotNull Component comp, Object constraints) {
        if (comp instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) comp);
        super.add(comp, constraints);
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        if (comp instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) comp);
        super.add(comp, constraints, index);
    }

    @Override
    public void add(PopupMenu popup) {
        if (popup instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) popup);
        super.add(popup);
    }

    //================================================================================
    // Default Swing Actions
    //================================================================================
    Action openSimulationAct = new SimpleAction("Open", e -> {
        pauseUI();
        try {
            File file = FileDialogUtil.openFileJFX(CobwebApplicationSwing.this, "Open Simulation Configuration", JFXFileExtFilter.COBWEB3D_XML);
            if (file != null) openFile(Cobweb3Serializer.loadConfig(file), false);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(CobwebApplicationSwing.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    Action saveSimulationAct = new SimpleAction("Save", e -> {
        pauseUI();
        String path = FileDialogUtil.saveFileJFX(CobwebApplicationSwing.this, "Save Simulation Configuration", JFXFileExtFilter.COBWEB3D_XML);
        if (path != null && !path.isEmpty()) saveSimulation(path);
    });

    public void saveSimulation(String savePath) {
        File sf = new File(savePath);
        if (sf.isHidden() || (sf.exists() && !sf.canWrite())) {
            JOptionPane.showMessageDialog(
                    this,
                    "Caution:  File \"" + savePath + "\" is NOT allowed to be written to.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try (OutputStream file = new FileOutputStream(sf)) {
                Cobweb3Serializer serializer = new Cobweb3Serializer();
                serializer.saveConfig(simRunner.getSimulation().simulationConfig, file);
            } catch (IOException ex) {
                throw new UserInputException("Save failed", ex);
            }
        }
    }

    /**
     * Copies the current simulation data being used to a temporary file, which
     * can be modified and saved by the user.
     */
    protected void openCurrentData() {
        String currentData = CURRENT_DATA_FILE_NAME;
        File cf = new File(currentData);
        cf.deleteOnExit();
        try {
            Cobweb3Serializer serializer = new Cobweb3Serializer();
            FileOutputStream outStream = new FileOutputStream(cf);
            serializer.saveConfig(simRunner.getSimulation().simulationConfig, outStream);
            outStream.close();
        } catch (IOException ex) {
            throw new UserInputException("Cannot open config file", ex);
        }
    }

    /**
     * Opens the simulation settings window with the current simulation file
     * data.  The user can modify and save the file here.  If the user tries
     * to overwrite data found in the default data file, a dialog box will be
     * created to tell the user the proper way to create new default data.
     */
    protected void openCurrentFile() {
        if (CURRENT_DATA_FILE_NAME.equals(currentFile) || currentFile == null) {
            throw new UserInputException("File not currently saved, use \"Modify Current Data\" instead");
        }
    }

    Action modifySimulationAct = new SimpleAction("Modify Simulation", e -> {
        pauseUI();
        openCurrentData();
    });
    Action modifySimulationFileAct = new SimpleAction("Modify Simulation File", e -> {
        pauseUI();
        openCurrentFile();
    });
    Action createNewDataAct = new SimpleAction("Create New Data", e -> {
        pauseUI();
        createNewData();
    });
    Action retrieveDefaultDataAct = new SimpleAction("Retrieve Default Data", e -> {
        pauseUI();
        retrieveDefaultData();
    });

    /**
     * Load simulation config.
     *
     * @param config       simulation configuration
     * @param continuation load this as a continuation of the current simulation?
     */
    @Override
    public File openFile(SimulationConfig config, boolean continuation) {
        File file = super.openFile(config, continuation);
        // TODO more organized way to deal with loading simulation configurations
        // TODO create new simRunner when starting new simulation, reuse when modifying
        if (simRunner.isRunning())
            simRunner.stop();
        if (!continuation) {
            simRunner.getSimulation().resetTime();
            simRunner.clearLogManager();
        }
        simRunner.loadSimulation(config);
        updateDynamicUI();
        return file;
    }
    Action reportData = new SimpleAction("Report", e -> {
        pauseUI();
        String path = FileDialogUtil.saveFileJFX(CobwebApplicationSwing.this, "Output Simulation Report", JFXFileExtFilter.LOG_TEXT);
        if (path != null && !path.isEmpty()) startSimulationReport(path);
    });
    Action quitAct = new SimpleAction("Quit", e -> quitApplication());
    Action savePopulationAct = new SimpleAction("Save Sample Population", e -> {
        // Open dialog to choose population size to be saved
        /* TODO
        HashMap<String, Object> result = openSaveSamplePopOptionsDialog();
        if (result != null){
            String option = (String)result.get("option");
            int amount = ((Integer)result.get("amount")).intValue();

            if (option != null && amount != -1) {
                // Open file dialog box
                FileDialog theDialog = new FileDialog(CobwebApplication.this,
                        "Choose a file to save state to", FileDialog.SAVE);
                theDialog.setFile("*.xml");
                theDialog.setVisible(true);
                if (theDialog.getFile() != null) {
                    int saveNum;

                    int agentCount = simRunner.getSimulation().theEnvironment.getAgentCount();
                    if (option.equals("percentage")) {
                        saveNum = agentCount * amount / 100;
                    } else {
                        saveNum = amount;
                    }
                    if (saveNum > agentCount)
                        saveNum = agentCount;

                    //Save population in the specified file.
                    PopulationSampler.savePopulation(simRunner.getSimulation(), theDialog.getDirectory() + theDialog.getFile(), saveNum);
                }
            }
        }*/
    });
    Action loadPopulationAct = new SimpleAction("Load Sample Population", e -> {
        // Open dialog to choose population size to be saved
        /* TODO
        HashMap<String, Object> result = openSaveSamplePopOptionsDialog();
                    ReplaceMergeCancel option = openInsertSamplePopReplaceDialog();

        if (option != ReplaceMergeCancel.CANCEL){
            //Select the XML file
            FileDialog theDialog = new FileDialog(CobwebApplication.this,
                    "Choose a file to load", FileDialog.LOAD);
            theDialog.setFile("*.xml");
            theDialog.setVisible(true);
            if (theDialog.getFile() != null) {
                String fileName = theDialog.getDirectory() + theDialog.getFile();
                //Load the XML file
                Set<String> incompatibilities = PopulationSampler.checkPopulationCompatible(simRunner.getSimulation(), fileName);
                if (!incompatibilities.isEmpty()) {
                    if (!askIgnoreIncompatibleDialog(incompatibilities))
                        return;
                }

                PopulationSampler.insertPopulation(simRunner.getSimulation(), fileName, option == ReplaceMergeCancel.REPLACE);
                simulatorUI.update(true);
            }
         }*/
    });
    //=====================================
    // Help Swing Actions
    //==============================
    Action openCreditsAct = new SimpleAction("Credits", e -> CreditsDialog.show());
    Action openAboutAct = new SimpleAction("About", e -> AboutDialog.show());

    public void updateDynamicUI() {
        // TODO:
        // if (simStatePanel != null) simStatePanel.simulationChanged();
        validate();
    }

    public void pauseUI() {
        if (simRunner != null) simRunner.stop();
    }

    /**
     * Opens an initial simulation settings file using the simulation settings
     * window.  The user can modify the simulation settings and save the
     * settings to a new file.  The method is invoked when the user selects
     * "Create New Data" located under "File" in the main tool bar.
     */
    private void createNewData() {
        SimulationConfigEditor editor = SimulationConfigEditor.show(this, INITIAL_OR_NEW_INPUT_FILE_NAME, false);
        if (editor.isOK()) openFile(editor.getConfig(), editor.isContinuation());
    }

    /**
     * Loads the default files simulation settings for the current simulation.
     * Uses the default file if available.  If not, then it will create a temporary
     * default data file to use.
     * <p>
     * <p> Used when the user selects "File" -> "Retrieve Default Data"
     */
    private void retrieveDefaultData() {
        // Two fashions for retrieving default data:
        // The first fashion for retrieving default data -- using the file default_data_(reserved).xml if one is
        // provided.
        String defaultData;
        boolean isTheFirstFashion = false;
        try {
            File df = new File(defaultData = ResourceRetriever.getResource("default_config.xml").getFile());
            if (df.exists()) {
                if (df.canWrite()) {
                    df.setReadOnly();
                }
                isTheFirstFashion = true;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        String tempDefaultData;
        File tdf;
        try {
            tdf = new File(tempDefaultData = ResourceRetriever.getResource("default_config.cwtemp").getFile());
            tdf.deleteOnExit();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if (isTheFirstFashion) {
            try {
                FileUtils.copyFile(defaultData, tempDefaultData);
            } catch (IOException ex) {
                isTheFirstFashion = false;
            }
        }

        if (!isTheFirstFashion) {
            if (tdf.exists()) {
                tdf.delete(); // delete the potential default_data file created by last time pressing
                // "Retrieve Default Data" menu.
            }
        }

        SimulationConfigEditor editor = SimulationConfigEditor.show(this, tempDefaultData, false);
        if (editor.isOK()) openFile(editor.getConfig(), editor.isContinuation());
    }

    /**
     * Opens a dialog box for the user to select the file he/she would like
     * to report to.
     */
    private void startSimulationReport(String path) {
        /* TODO
        try {
            simRunner.report(new FileWriter(path, false));
        } catch (IOException ex) {
            throw new UserInputException("Can't create report file!", ex);
        }*/
    }
}
