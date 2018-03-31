package cobweb3d.ui.swing.config.logging;

import cobweb3d.impl.Simulation;
import cobweb3d.impl.logging.LogConfig;

import javax.swing.*;
import java.awt.*;

public class LogConfigEditor {
    public static final long serialVersionUID = 0xA9967684A8375BC0L;
    private static final String WINDOW_TITLE = "Log Settings";
    private final JDialog dialog;

    private LogConfigEditor(Window parent, LogConfig logConfig) {
        dialog = new JDialog(parent, WINDOW_TITLE, Dialog.DEFAULT_MODALITY_TYPE);

        JPanel j = new JPanel();
        j.setLayout(new BoxLayout(j, BoxLayout.Y_AXIS));

        JButton okButton = new JButton("OK");
        okButton.setMaximumSize(new Dimension(80, 20));
        okButton.addActionListener(e -> closeEditor());

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(okButton);

        // Add the tabbed pane to this panel.
        LogConfigPage logConfigPage = new LogConfigPage(logConfig);
        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.addTab("Enabled Loggers", logConfigPage.getPanel());
        j.add(jTabbedPane, BorderLayout.CENTER);
        j.add(buttons, BorderLayout.SOUTH);
        j.setPreferredSize(j.getPreferredSize());//new Dimension(800, 600));

        dialog.getRootPane().setDefaultButton(okButton);
        dialog.add(j);
        dialog.pack();
    }

    /**
     * Create the SimulationConfigEditor and show it. For thread safety, this method should be invoked from the event-dispatching thread.
     */
    public static LogConfigEditor show(Window parent, Simulation simulation) {
        return show(parent, simulation.simulationConfig.logConfig);
    }

    /**
     * Create the SimulationConfigEditor and show it. For thread safety, this method should be invoked from the event-dispatching thread.
     */
    public static LogConfigEditor show(Window parent, LogConfig logConfig) {
        // Create and set up the content pane.
        LogConfigEditor configEditor = new LogConfigEditor(parent, logConfig);
        configEditor.show();
        return configEditor;
    }

    private void show() {
        dialog.setVisible(true);
    }

    private void closeEditor() {
        dialog.setVisible(false);
        dialog.dispose();
    }
}
