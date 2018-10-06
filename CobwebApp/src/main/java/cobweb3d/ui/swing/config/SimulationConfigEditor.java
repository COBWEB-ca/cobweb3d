package cobweb3d.ui.swing.config;

import cobweb3d.impl.SimulationConfig;
import cobweb3d.io.Cobweb3Serializer;
import cobweb3d.plugins.diminish.ui.DiminishConfigPage;
import cobweb3d.plugins.exchange.ui.ExchangeConfigPage;
import cobweb3d.plugins.food.ui.ConsumptionConfigPage;
import cobweb3d.plugins.ported.disease.ui.DiseaseConfigPage;
import cobweb3d.plugins.reproduction.ui.ReproductionConfigPage;
import cobweb3d.plugins.resources.ui.ResourceConfigPage;
import cobweb3d.plugins.transform.ui.TransformationConfigPage;
import cobweb3d.plugins.vision.ui.VisionConfigPage;
import cobweb3d.ui.application.CobwebApplication;
import cobweb3d.ui.exceptions.UserInputException;
import cobweb3d.ui.swing.config.mutator.MutatorConfigPage;
import cobweb3d.ui.swing.config.pages.AgentConfigPage;
import cobweb3d.ui.swing.config.pages.EnvironmentConfigPage;
import cobweb3d.ui.util.TypeColorEnumeration;
import cobwebutil.io.ChoiceCatalog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimulationConfigEditor implements ConfigRefresher {
    public static final long serialVersionUID = 0xA9967684A8375BC0L;
    private static final String WINDOW_TITLE = "Simulation Settings";
    private final JTabbedPane tabbedPane;
    private final JDialog dialog;
    private Cobweb3Serializer serializer = new Cobweb3Serializer();
    private Logger logger = Logger.getLogger("COBWEB 3D");
    private SimulationConfig simConfig;
    private String filePath;
    private boolean modifyExisting;
    private boolean ok;

    // SimulationConfigEditor Special Constructor
    private SimulationConfigEditor(Window parent, String filename, boolean allowModify) {
        dialog = new JDialog(parent, WINDOW_TITLE, Dialog.DEFAULT_MODALITY_TYPE);
        JPanel j = new JPanel();
        j.setLayout(new BoxLayout(j, BoxLayout.Y_AXIS));

        filePath = filename;
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        modifyExisting = allowModify;

        File f = new File(filePath);

        if (f.exists()) {
            try {
                simConfig = Cobweb3Serializer.loadConfig(filePath);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Cannot open config file", ex);
                setDefault();
            }
        } else setDefault();

        setupConfigPages();

        JButton okButton = new JButton("OK");
        okButton.setMaximumSize(new Dimension(80, 20));
        okButton.addActionListener(new OkButtonListener());

        JButton saveButton = new JButton("Save As...");
        saveButton.setMaximumSize(new Dimension(80, 20));
        saveButton.addActionListener(new SaveAsButtonListener());

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(saveButton);
        buttons.add(okButton);

        // Add the tabbed pane to this panel.
        j.add(tabbedPane, BorderLayout.CENTER);
        j.add(buttons, BorderLayout.SOUTH);
        j.setPreferredSize(new Dimension(750, 513));


        dialog.getRootPane().setDefaultButton(okButton);
        //dialog.setContentPane(j);
        dialog.add(j);
        dialog.pack();

        File filePath;
        if (simConfig.fileName == null ||
                (filePath = new File(simConfig.fileName)).getName() == null)
            filePath = new File(CobwebApplication.DEFAULT_DATA_FILE_NAME + CobwebApplication.CONFIG_FILE_EXTENSION);
        dialog.setTitle(WINDOW_TITLE + " - " + filePath.getName());
    }

    /**
     * Create the SimulationConfigEditor and show it. For thread safety, this method should be invoked from the event-dispatching thread.
     */
    public static SimulationConfigEditor show(Window parent, String filename, boolean allowModify) {
        // Create and set up the content pane.
        SimulationConfigEditor configEditor = new SimulationConfigEditor(parent, filename, allowModify);
        configEditor.show();
        return configEditor;
    }

    private void show() {
        dialog.setVisible(true);
    }

    public SimulationConfig getConfig() {
        return simConfig;
    }

    public boolean isContinuation() {
        return modifyExisting && simConfig.isContinuation();
    }

    public boolean isOK() {
        return ok;
    }

    /**
     * This openFileDialog method is invoked by pressing the "Save" button
     */
    public boolean saveAsDialog() {
        FileDialog saveDialog = new FileDialog(dialog, "Choose a file to save state to", java.awt.FileDialog.SAVE);
        saveDialog.setFile("*.xml");
        saveDialog.setVisible(true);
        if (saveDialog.getFile() != null) {
            try {
                // Handle a readonly file
                String savingFile = saveDialog.getDirectory() + saveDialog.getFile();
                File sf = new File(savingFile);
                if (sf.isHidden() || (sf.exists() && !sf.canWrite())) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Caution:  File \"" + savingFile + "\" is NOT allowed to be written to.", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    String filePath = saveDialog.getDirectory() + saveDialog.getFile();
                    FileOutputStream configStream = new FileOutputStream(filePath);
                    serializer.saveConfig(simConfig, configStream);
                    configStream.close();

                    simConfig = Cobweb3Serializer.loadConfig(filePath);

                    return true;
                }
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Cannot save config", ex);
                JOptionPane.showMessageDialog(dialog,
                        "Save failed: " + ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        return false;
    }

    private void setDefault() {
        simConfig = new SimulationConfig();
    }

    @Override
    public void refreshConfig() {
        simConfig.setAgentTypes(simConfig.getAgentTypes());
        setupConfigPages();
    }

    private void setupConfigPages() {
        tabbedPane.removeAll();

        try {
            EnvironmentConfigPage environmentConfigPage = new EnvironmentConfigPage(simConfig, modifyExisting, this);
            tabbedPane.addTab("Environment", environmentConfigPage.getPanel());
        } catch (NoSuchFieldException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }

        AgentConfigPage agentPage = new AgentConfigPage(simConfig.agentParams.agentParams);
        tabbedPane.addTab("Agents", agentPage.getPanel());

        MutatorConfigPage mutatorConfigPage = new MutatorConfigPage(simConfig.mutatorConfig);
        tabbedPane.addTab("Plugins", mutatorConfigPage.getPanel());

        ReproductionConfigPage reproductionConfigPage = new ReproductionConfigPage(simConfig.reproductionParams,
                new ChoiceCatalog(),
                new TypeColorEnumeration(simConfig.agentParams.getPerTypeParams()));
        tabbedPane.addTab("Reproduction", reproductionConfigPage.getPanel());

        ConsumptionConfigPage consumptionConfigPage = new ConsumptionConfigPage(simConfig.consumptionParams,
                new ChoiceCatalog(),
                new TypeColorEnumeration(simConfig.agentParams.getPerTypeParams()));
        tabbedPane.addTab("Consumption", consumptionConfigPage.getPanel());

        DiminishConfigPage diminishConfigPage = new DiminishConfigPage(simConfig.diminishParams,
                new ChoiceCatalog(),
                new TypeColorEnumeration(simConfig.agentParams.getPerTypeParams()));
        tabbedPane.addTab("Diminish", diminishConfigPage.getPanel());

        ExchangeConfigPage exchangeConfigPage = new ExchangeConfigPage(simConfig.exchangeParams,
                new TypeColorEnumeration(simConfig.agentParams.getPerTypeParams()), this);
        tabbedPane.addTab("Exchange", exchangeConfigPage.getPanel());

        TransformationConfigPage transformationConfigPage = new TransformationConfigPage(simConfig.transformationParams,
                new ChoiceCatalog(),
                new TypeColorEnumeration(simConfig.agentParams.getPerTypeParams()));
        tabbedPane.addTab("Transformation", transformationConfigPage.getPanel());

        DiseaseConfigPage diseaseConfigPage = new DiseaseConfigPage(simConfig.diseaseParams,
                Cobweb3Serializer.getChoiceCatalog(),
                new TypeColorEnumeration(simConfig.agentParams.getPerTypeParams()));
        tabbedPane.addTab("Disease", diseaseConfigPage.getPanel());


        VisionConfigPage visionConfigPage = new VisionConfigPage(simConfig.visionParams,
                new ChoiceCatalog(),
                new TypeColorEnumeration(simConfig.agentParams.getPerTypeParams()));
        tabbedPane.addTab("Vision", visionConfigPage.getPanel());

        ResourceConfigPage resourceConfigPage = new ResourceConfigPage(simConfig.resourceParams,
                new ChoiceCatalog(), new TypeColorEnumeration(simConfig.agentParams.getPerTypeParams()));
        tabbedPane.addTab("Resource", resourceConfigPage.getPanel());
    }

    private void validateSettings() {
        try {
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                Component tab = tabbedPane.getTabComponentAt(i);
                if (tab instanceof ConfigPage) ((ConfigPage) tab).validateUI();
            }
        } catch (IllegalArgumentException ex) {
            throw new UserInputException("Parameter error: " + ex.getMessage(), ex);
        }
    }

    private void removeOldPage(ConfigPage r) {
        if (r != null) {
            tabbedPane.remove(r.getPanel());
        }
    }

    private void closeEditor() {
        ok = true;
        dialog.setVisible(false);
        dialog.dispose();
    }

    private final class OkButtonListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            validateSettings();

            /* write UI info to xml file */
            try {
                serializer.saveConfig(simConfig, new FileOutputStream(filePath));
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Cannot write file! Make sure your file is not read-only.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

            /* create a new parser for the xml file */
            try {
                simConfig = Cobweb3Serializer.loadConfig(filePath);
            } catch (IOException ex) {
                throw new UserInputException("Cannot open file!", ex);
            }

            closeEditor();
        }
    }

    private final class SaveAsButtonListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            validateSettings();

            if (saveAsDialog()) {
                closeEditor();
            }
        }
    }
}
