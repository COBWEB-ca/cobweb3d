package cobweb3d.plugins.exchange.ui;

import cobweb3d.plugins.exchange.ExchangeParams;
import cobweb3d.ui.swing.config.ConfigPage;
import cobweb3d.ui.swing.config.ConfigRefresher;
import cobweb3d.ui.swing.config.Util;
import cobweb3d.ui.util.SpringUtilities;
import cobwebutil.swing.ColorLookup;

import javax.swing.*;
import java.awt.*;


public class ExchangeConfigPage implements ConfigPage {
    private ExchangeParams params;
    private ConfigRefresher refresher;

    private JPanel mainPanel;

    private ExchangeAgentConfigPage exchangeAgentConfigPage;
    private ExchangeAgentPairConfigPage exchangeAgentPairConfigPage;

    public ExchangeConfigPage(ExchangeParams exchangeParams, ColorLookup agentColors, ConfigRefresher configRefresher) {
        this.params = exchangeParams;
        this.refresher = refresher;
        mainPanel = new JPanel(new SpringLayout());
        exchangeAgentConfigPage = new ExchangeAgentConfigPage(params, agentColors);
        mainPanel.add(exchangeAgentConfigPage.getPanel());
        mainPanel.add(makePairPanel(params, agentColors));
        //ExchangeLogPage exchangeLogPage = new ExchangeLogPage(params);
        // mainPanel.add(exchangeLogPage);
       /* JPanel logGrpPanel = new JPanel();
        Util.makeGroupPanel(logGrpPanel , "Logging");
        logGrpPanel.setLayout(new BorderLayout());
        JLabel pathLabel = new JLabel("Path: ");
        JButton changePath = new JButton("Set Log");
        logGrpPanel.add(pathLabel);
        logGrpPanel.add(changePath);*/

        SpringUtilities.makeCompactGrid(mainPanel, 2, 1, 0, 0, 0, 0, 0, 192);
    }

    public static void makeOptionsTable(JPanel fieldPane, int items) {
        fieldPane.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(fieldPane, items, 2, 0, 0, 16, 0, 50, 0);
    }

    private JPanel makePairPanel(ExchangeParams exchangeParams, ColorLookup agentColors) {
        JPanel grpPanel = new JPanel();
        Util.makeGroupPanel(grpPanel, "Exchange Settings");
        grpPanel.setLayout(new BorderLayout());
        exchangeAgentPairConfigPage = new ExchangeAgentPairConfigPage(exchangeParams, agentColors);
        grpPanel.add(exchangeAgentPairConfigPage.getPanel());
        return grpPanel;
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public void validateUI() throws IllegalArgumentException {
        if (exchangeAgentConfigPage != null) exchangeAgentConfigPage.validateUI();
    }
}