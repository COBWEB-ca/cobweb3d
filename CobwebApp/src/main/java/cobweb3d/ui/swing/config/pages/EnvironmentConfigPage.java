/**
 *
 */
package cobweb3d.ui.swing.config.pages;

import cobweb3d.core.params.BaseEnvironmentParams;
import cobweb3d.impl.SimulationConfig;
import cobweb3d.ui.config.FieldPropertyAccessor;
import cobweb3d.ui.config.SetterPropertyAccessor;
import cobweb3d.ui.swing.config.ConfigPage;
import cobweb3d.ui.swing.config.ConfigRefresher;
import cobweb3d.ui.swing.config.Util;
import cobweb3d.ui.util.SpringUtilities;
import cobweb3d.ui.util.binding.BoundCheckBox;
import cobweb3d.ui.util.binding.BoundJFormattedTextField;
import cobwebutil.swing.SeedRandomActionListener;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

/**
 * @author Igor
 */
public class EnvironmentConfigPage implements ConfigPage {
    private JPanel thePanel;

    private SimulationConfig params;

    private ConfigRefresher refresher;

    public EnvironmentConfigPage(SimulationConfig theParams, boolean allowKeep, ConfigRefresher refresher) throws NoSuchFieldException, NoSuchMethodException {
        this.params = theParams;
        this.refresher = refresher;
        thePanel = new JPanel(new SpringLayout());

        JPanel panel11 = makeEnvironmentPanel();
        thePanel.add(panel11);

        JPanel panel12 = makeTransitionPanel(allowKeep);
        thePanel.add(panel12);

        JPanel panel14 = makeRandomPanel();

        thePanel.add(panel14);

        SpringUtilities.makeCompactGrid(thePanel, 1, 3, 0, 0, 0, 0, 0, 0);
    }

    private static void makeOptionsTable(JPanel fieldPane, int items) {
        fieldPane.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(fieldPane, items, 2, 0, 0, 16, 0, 50, 0);
    }

    private JPanel makeEnvironmentPanel() throws NoSuchFieldException, NoSuchMethodException {
        JPanel panel11 = new JPanel();
        Util.makeGroupPanel(panel11, "Environment Settings");
        JPanel fieldPane = new JPanel();

        BoundJFormattedTextField Width = new BoundJFormattedTextField(params.envParams,
                new FieldPropertyAccessor(BaseEnvironmentParams.class.getField("width")),
                NumberFormat.getIntegerInstance());
        fieldPane.add(new JLabel(Width.getLabelText()));
        fieldPane.add(Width);

        BoundJFormattedTextField Height = new BoundJFormattedTextField(params.envParams,
                new FieldPropertyAccessor(BaseEnvironmentParams.class.getField("height")),
                NumberFormat.getIntegerInstance());
        fieldPane.add(new JLabel(Height.getLabelText()));
        fieldPane.add(Height);

        BoundJFormattedTextField depth = new BoundJFormattedTextField(params.envParams,
                new FieldPropertyAccessor(BaseEnvironmentParams.class.getField("depth")),
                NumberFormat.getIntegerInstance());
        fieldPane.add(new JLabel(depth.getLabelText()));
        fieldPane.add(depth);

        /*BoundCheckBox wrap = new BoundCheckBox(params.envParams,
                new FieldPropertyAccessor(BaseEnvironmentParams.class.getField("wrapMap")));
        fieldPane.add(new JLabel(wrap.getLabelText()));
        fieldPane.add(wrap);*/

        BoundJFormattedTextField AgentNum = new BoundJFormattedTextField(params,
                new SetterPropertyAccessor(SimulationConfig.class.getMethod("setAgentTypes", int.class)),
                NumberFormat.getIntegerInstance());
        AgentNum.addPropertyChangeListener("value", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                refresher.refreshConfig();
            }
        });

        fieldPane.add(new JLabel(AgentNum.getLabelText()));
        fieldPane.add(AgentNum);

        panel11.add(fieldPane, BorderLayout.CENTER);

        makeOptionsTable(fieldPane, 4);
        return panel11;
    }

    private JPanel makeTransitionPanel(boolean allowKeep) {
        JPanel fieldPane;
        JPanel panel12 = new JPanel();
        Util.makeGroupPanel(panel12, "Environment Transition Settings");

        fieldPane = new JPanel();
        BoundCheckBox keepOldAgents = null;
        BoundCheckBox spawnNewAgents = null;
        int count = 0;
        try {
            keepOldAgents = new BoundCheckBox(params, new FieldPropertyAccessor(SimulationConfig.class.getField("keepOldAgents")));
            fieldPane.add(new JLabel(keepOldAgents.getLabelText()));
            fieldPane.add(keepOldAgents);
            count++;
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }

        try {
            spawnNewAgents = new BoundCheckBox(params, new FieldPropertyAccessor(SimulationConfig.class.getField("spawnNewAgents")));
            fieldPane.add(new JLabel(spawnNewAgents.getLabelText()));
            fieldPane.add(spawnNewAgents);
            count++;
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }

        makeOptionsTable(fieldPane, count);

        panel12.add(fieldPane);


        if (!allowKeep) {
            if (keepOldAgents != null) keepOldAgents.setEnabled(false);
            if (keepOldAgents != null) keepOldAgents.setSelected(false);
        }

        return panel12;
    }

    private JPanel makeRandomPanel() throws NoSuchFieldException {
        JPanel fieldPane;
        JPanel panel14 = new JPanel();
        Util.makeGroupPanel(panel14, "Random Variables");
        fieldPane = new JPanel(new GridLayout(3, 1));

        /*BoundJFormattedTextField initialStones = new BoundJFormattedTextField(params.envParams,
                new FieldPropertyAccessor(BaseEnvironmentParams.class.getField("initialStones")),
                NumberFormat.getIntegerInstance());
        fieldPane.add(new JLabel(initialStones.getLabelText()));
        fieldPane.add(initialStones);*/

        BoundJFormattedTextField randomSeed = new BoundJFormattedTextField(params, new FieldPropertyAccessor(SimulationConfig.class.getField("randomSeed")), NumberFormat.getIntegerInstance());
        JButton makeRandom = new JButton("Generate");
        makeRandom.addActionListener(new SeedRandomActionListener(randomSeed));
        fieldPane.add(new JLabel(randomSeed.getLabelText()));
        fieldPane.add(randomSeed);

        fieldPane.add(new JPanel());
        fieldPane.add(makeRandom);

        panel14.add(fieldPane, BorderLayout.EAST);
        makeOptionsTable(fieldPane, 2);
        return panel14;
    }

    /* (non-Javadoc)
     * @see driver.config.ConfigPage#getPanel()
     */
    @Override
    public JPanel getPanel() {
        return thePanel;
    }

    @Override
    public void validateUI() throws IllegalArgumentException {
        // Nothing
    }
}
