package cobweb3d.plugins.abiotic.ui;

import cobweb3d.plugins.abiotic.AbioticParams;
import cobweb3d.ui.swing.config.ConfigPage;

import javax.swing.*;

public class AbioticConfigPage implements ConfigPage {

    private AbioticParams params;
    private JPanel mainPanel;
    private AbioticAgentConfigPage abioticAgentConfigPage;
    private AbioticConfigPage abioticConfigPage;

    @Override
    public JPanel getPanel() {
        return null;
    }

    @Override
    public void validateUI() throws IllegalArgumentException {

    }
}
