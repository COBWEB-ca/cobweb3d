package cobweb3d.plugins.exchange.ui;

import cobweb3d.plugins.exchange.ExchangeAgentPairDynamicParams;
import cobweb3d.plugins.exchange.ExchangeAgentPairParams;
import cobweb3d.plugins.exchange.ExchangeParams;
import cobweb3d.ui.config.FieldPropertyAccessor;
import cobweb3d.ui.swing.config.ConfigPage;
import cobweb3d.ui.util.SpringUtilities;
import cobweb3d.ui.util.binding.BoundCheckBox;
import cobweb3d.ui.util.binding.BoundJFormattedTextField;
import cobweb3d.ui.util.binding.BoundJSpinner;
import cobwebutil.swing.ColorLookup;
import cobwebutil.swing.SimpleAction;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

import static java.awt.GridBagConstraints.HORIZONTAL;

public class ExchangeAgentPairConfigPage extends JPanel implements ConfigPage {
    private static Insets INSETS = new Insets(8, 8, 8, 8);
    private ExchangeParams params;
    private String[] agentTypeArray;
    private ColorLookup colorLookup;
    private JComboBox<String> agentOneCbx;
    private JComboBox<String> agentTwoCbx;
    private ExchangePanel exchangePanel;

    public ExchangeAgentPairConfigPage(ExchangeParams params, ColorLookup agentColors) {
        this.params = params;
        this.colorLookup = agentColors;
        agentTypeArray = new String[params.agentParams.length];
        for (int i = 0; i < params.agentParams.length; i++) {
            agentTypeArray[i] = "Agent " + (i + 1);
        }
        buildPanel();
    }

    private void buildPanel() {
        setLayout(new GridBagLayout());

        addComponent(agentOneCbx = new JComboBox<>(agentTypeArray), 0, 0, 1, 1, INSETS);
        addComponent(new JLabel("and"), 0, 1, 1, 1, 0, INSETS);
        addComponent(agentTwoCbx = new JComboBox<>(agentTypeArray), 0, 2, 1, 1, INSETS);
        popAndRefreshExchangePanel(params.getPairParams(agentOneCbx.getSelectedIndex(), agentTwoCbx.getSelectedIndex()));

        agentOneCbx.setAction(new SimpleAction("", e -> popAndRefreshExchangePanel(params.getPairParams(agentOneCbx.getSelectedIndex(), agentTwoCbx.getSelectedIndex()))));
        agentTwoCbx.setAction(new SimpleAction("", e -> popAndRefreshExchangePanel(params.getPairParams(agentOneCbx.getSelectedIndex(), agentTwoCbx.getSelectedIndex()))));
    }

    private void popAndRefreshExchangePanel(ExchangeAgentPairParams pairParams) {
        if (exchangePanel != null) {
            exchangePanel.dispose();
            remove(exchangePanel);
        }
        exchangePanel = new ExchangePanel(pairParams);
        addComponent(exchangePanel, 1, 0, GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, INSETS);
        agentOneCbx.setBackground(colorLookup.getColor(agentOneCbx.getSelectedIndex(), 100));
        agentTwoCbx.setBackground(colorLookup.getColor(agentTwoCbx.getSelectedIndex(), 100));
        revalidate();
        repaint();
    }

    private void addComponent(Component component, int row,
                              int column, int width, int height, Insets inset1) {
        addComponent(component, row, column, width, height, 1, inset1);
    }


    private void addComponent(Component component, int row,
                              int column, int width, int height, int weightx, Insets inset1) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = HORIZONTAL;
        c.weightx = weightx;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = column;
        c.gridy = row;
        c.gridwidth = width;
        c.gridheight = height;
        c.insets = inset1;
        add(component, c);
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void validateUI() throws IllegalArgumentException {
        // Nothing.
    }

    private class ExchangePanel extends JPanel {
        ExchangeAgentPairParams pairParams;

        BoundJFormattedTextField xTransfer;
        BoundJFormattedTextField yTransfer;
        BoundCheckBox dynamicQuanitites;
        BoundJSpinner lowerLim;
        BoundJSpinner upperLim;
        BoundJSpinner incrementSpinner;

        public ExchangePanel(ExchangeAgentPairParams pairParams) {
            if (pairParams == null) pairParams = new ExchangeAgentPairParams(-1, -1);
            this.pairParams = pairParams;
            try {
                xTransfer = new BoundJFormattedTextField(pairParams,
                        new FieldPropertyAccessor(ExchangeAgentPairParams.class.getField("quantXTransfer")),
                        NumberFormat.getIntegerInstance());
                add(new JLabel(xTransfer.getLabelText()));
                add(xTransfer);

                yTransfer = new BoundJFormattedTextField(pairParams,
                        new FieldPropertyAccessor(ExchangeAgentPairParams.class.getField("quantYTransfer")),
                        NumberFormat.getIntegerInstance());
                add(new JLabel(yTransfer.getLabelText()));
                add(yTransfer);

                dynamicQuanitites = new BoundCheckBox(pairParams.dynParams, new FieldPropertyAccessor(ExchangeAgentPairDynamicParams.class.getField("enabled")));
                dynamicQuanitites.setText(dynamicQuanitites.getLabelText());
                dynamicQuanitites.setMargin(new Insets(4, -2, 0, 0));
                dynamicQuanitites.setHorizontalTextPosition(SwingConstants.LEFT);
                dynamicQuanitites.addActionListener(l -> {
                    boolean selected = ((AbstractButton) l.getSource()).isSelected();
                    lowerLim.setEnabled(selected);
                    upperLim.setEnabled(selected);
                    incrementSpinner.setEnabled(selected);
                });
                add(dynamicQuanitites);

                if (pairParams.dynParams.lowerBound < 0) pairParams.dynParams.lowerBound = 0;
                lowerLim = new BoundJSpinner(pairParams.dynParams,
                        new FieldPropertyAccessor(ExchangeAgentPairDynamicParams.class.getField("lowerBound")), new SpinnerNumberModel(1, -0.01f, Float.MAX_VALUE, 0.1));
                add(new JLabel(lowerLim.getLabelText()));
                ((JSpinner.DefaultEditor) lowerLim.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
                add(lowerLim);

                if (pairParams.dynParams.upperBound < 0) pairParams.dynParams.upperBound = 0;
                upperLim = new BoundJSpinner(pairParams.dynParams,
                        new FieldPropertyAccessor(ExchangeAgentPairDynamicParams.class.getField("upperBound")), new SpinnerNumberModel(1, -0.01f, Float.MAX_VALUE, 0.1));
                add(new JLabel(upperLim.getLabelText()));
                ((JSpinner.DefaultEditor) upperLim.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
                add(upperLim);

                if (pairParams.dynParams.increment <= 0) pairParams.dynParams.increment = 1;
                incrementSpinner = new BoundJSpinner(pairParams.dynParams,
                        new FieldPropertyAccessor(ExchangeAgentPairDynamicParams.class.getField("increment")), new SpinnerNumberModel(1, 0.01f, Float.MAX_VALUE, 0.1));
                ((JSpinner.DefaultEditor) incrementSpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
                add(new JLabel(incrementSpinner.getLabelText()));
                add(incrementSpinner);

                lowerLim.setEnabled(pairParams.dynParams.enabled);
                upperLim.setEnabled(pairParams.dynParams.enabled);
                incrementSpinner.setEnabled(pairParams.dynParams.enabled);

                // ExchangeConfigPage.makeOptionsTable(this, 6);
                setLayout(new SpringLayout());
                SpringUtilities.makeCompactGrid(this, 11, 1, 0, 0, 0, 0, 50, 0);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public ExchangeAgentPairParams pairParams() {
            return pairParams;
        }

        public void dispose() {
            pairParams = null;
            remove(xTransfer);
            remove(yTransfer);
            remove(dynamicQuanitites);
            remove(lowerLim);
            remove(upperLim);
            remove(incrementSpinner);
            // remove(increment);
            xTransfer = null;
            yTransfer = null;
            dynamicQuanitites = null;
            lowerLim = null;
            upperLim = null;
            // increment = null;
            incrementSpinner = null;
        }
    }
}
