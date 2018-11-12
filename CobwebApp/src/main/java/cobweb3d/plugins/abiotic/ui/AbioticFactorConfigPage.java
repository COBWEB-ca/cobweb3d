package cobweb3d.plugins.abiotic.ui;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.environment.BaseEnvironment;
import cobweb3d.core.environment.Topology;
import cobweb3d.plugins.abiotic.AbioticParams;
import cobweb3d.plugins.abiotic.factor.AbioticFactor;
import cobweb3d.plugins.abiotic.factor.patterns.Bands;
import cobweb3d.plugins.abiotic.factor.patterns.HorizontalBands;
import cobweb3d.plugins.abiotic.factor.patterns.VerticalBands;
import cobweb3d.ui.swing.config.ConfigPage;
import cobweb3d.ui.swing.config.ConfigRefresher;
import cobweb3d.ui.swing.config.TableConfigPage;
import cobweb3d.ui.swing.config.Util;
import cobwebutil.RandomNoGenerator;
import cobwebutil.swing.ListManipulator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class AbioticFactorConfigPage implements ConfigPage {
    private AbioticParams params;

    private JPanel myPanel;

    private JPanel factorConfigPanel;

    private static final List<AbioticFactor> PATTERNS = Arrays.asList(
            new HorizontalBands(),
            new VerticalBands()
    );

    private ConfigRefresher refresher;

    public AbioticFactorConfigPage(AbioticParams abioticParams, ConfigRefresher simulationConfigEditor) {
        this.params = abioticParams;
        this.refresher = simulationConfigEditor;

        myPanel = new JPanel();
        myPanel.setLayout(new BorderLayout());

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JComponent optionPanel = setupAvailable();

        JComponent selectedPanel = setupSelectedList();

        JButton addPheno = new JButton(addFactor);
        JButton remPheno = new JButton(removeFactor);
        JPanel buttons = new JPanel();
        buttons.add(addPheno);
        buttons.add(remPheno);

        left.add(optionPanel);
        left.add(buttons);
        left.add(selectedPanel);

        myPanel.add(left, BorderLayout.WEST);

        factorConfigPanel = new JPanel(new BorderLayout());

        myPanel.add(factorConfigPanel);

        Util.makeGroupPanel(myPanel, "Abiotic Factors");
    }

    private ListModel<AbioticFactor> modelOptions;

    private ListManipulator<AbioticFactor> modelSelected;

    private JList<AbioticFactor> listSelected;

    private JList<AbioticFactor> listOptions;

    private JScrollPane setupAvailable() {
        modelOptions = new ListManipulator<>(PATTERNS);

        listOptions = new JList<AbioticFactor>(modelOptions);
        listOptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listOptions.setLayoutOrientation(JList.VERTICAL);
        listOptions.setVisibleRowCount(-1);
        listOptions.setCellRenderer(new FactorNameRenderer());
        JScrollPane scroller = new JScrollPane(listOptions);
        scroller.setPreferredSize(new Dimension(240, 500));

        Util.makeGroupPanel(scroller, "Available Patterns");
        return scroller;
    }

    private JComponent setupSelectedList() {
        modelSelected = new ListManipulator<>(params.factors);

        listSelected = new JList<AbioticFactor>(modelSelected);
        listSelected.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSelected.setLayoutOrientation(JList.VERTICAL);
        listSelected.setVisibleRowCount(-1);
        listSelected.setCellRenderer(new ActiveFactorRenderer());
        listSelected.addListSelectionListener(new EditorSelector());
        JScrollPane scroller = new JScrollPane(listSelected);
        scroller.setPreferredSize(new Dimension(240, 500));

        Util.makeGroupPanel(scroller, "Active Factors");
        return scroller;
    }

    private class EditorSelector implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting())
                return;
            int index = listSelected.getSelectedIndex();
            editFactor(index);
        }
    }

    int lastEditorIndex = -1;

    private void refreshEditor() {
        editFactor(lastEditorIndex);
    }

    private void editFactor(int index) {
        factorConfigPanel.removeAll();

        lastEditorIndex = index;

        if (index < 0)
            return;

        AbioticFactor selectedFactor = params.factors.get(index);

        String editorName = describeActiveFactor(selectedFactor, index);
        JPanel editorPanel = null;
        if (selectedFactor instanceof Bands) {
            Bands bands = (Bands) selectedFactor;
            editorPanel = getBandsEditor(bands);
        } else {
            editorPanel = getDefaultEditor(selectedFactor);
        }
        if (editorPanel != null) {
            Util.makeGroupPanel(editorPanel, editorName);
            factorConfigPanel.add(editorPanel);
        }
        //factorConfigPanel.add(getFactorPreview(selectedFactor), BorderLayout.EAST);
        factorConfigPanel.validate();
        factorConfigPanel.repaint();
    }

    private static Component getFactorPreview(final AbioticFactor selectedFactor) {
        final JComponent preview = new JComponent() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                float max = selectedFactor.getMax();
                float min = selectedFactor.getMin();
                if ( min == max) {
                    g.setColor(Color.GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                else {
                    BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

                    int w = image.getWidth();
                    int h = image.getHeight();
                    for (int y = 0; y < h; y++) {
                        for (int x = 0; x < w; x++) {
                            /** the next line is currently wrong!!!!!!!!!!!! (since z shouldn't always be 0)*/
                            float v = selectedFactor.getValue((float) x / w, (float)y / h, 0);
                            float normalV = (v - min) / (max - min);
                            int rgb = Color.HSBtoRGB((1 - normalV) * 2 / 3, 1f, 1f);
                            image.setRGB(x, y, rgb);
                        }
                    }
                    g.drawImage(image, 0,0, this);
                }
            }

            private static final long serialVersionUID = 1L;
        };


        final SimulationTimerStub timerSimStub = new SimulationTimerStub();

        final JLabel stepCounter = new JLabel();


        final Timer refreshTimer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerSimStub.time++;
                selectedFactor.update(timerSimStub);
                stepCounter.setText("<html>Tick: " + timerSimStub.time + "<br>"
                        + "Center Value: " + valueFormat.format(selectedFactor.getValue(.5f, .5f, .5f)) + "</html>");
                if (!preview.isShowing()) {
                    ((Timer)e.getSource()).stop();
                    return;
                }

                preview.repaint();
            }
        });
        refreshTimer.start();

        preview.setPreferredSize(new Dimension(150, 150));
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new BorderLayout());
        Util.makeGroupPanel(previewPanel, "Preview");
        previewPanel.add(preview, BorderLayout.NORTH);
        previewPanel.add(stepCounter);

        return previewPanel;
    }


    private static NumberFormat valueFormat = new DecimalFormat("#0.###");

    private static class SimulationTimerStub implements SimulationTimeSpace {

        @Override
        public RandomNoGenerator getRandom() {
            return null;
        }

        @Override
        public Topology getTopology() {
            return null;
        }

        @Override
        public BaseEnvironment getEnvironment() {
            return null;
        }

        public long time = 0;

        @Override
        public long getTime() {
            return time;
        }
    }

    private JPanel getBandsEditor(final Bands factor) {
        TableConfigPage<Bands> editor = new TableConfigPage<>(
                new Bands[] { factor },
                null,
                "Value");
        editor.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                refreshEditor();
            }
        });

        final JPanel table = editor.getPanel();

        JPanel group = new JPanel(new BorderLayout());

        group.add(table);

        JPanel buttons = new JPanel();

        Action addBand = new AbstractAction("Add New Band") {
            @Override
            public void actionPerformed(ActionEvent e) {
                factor.bands.add(0f);
                refreshEditor();
            }
            private static final long serialVersionUID = 1L;
        };
        Action removeBand = new AbstractAction("Remove Last Band") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (factor.bands.size() <= 1)
                    return;
                factor.bands.remove(factor.bands.size()-1);
                refreshEditor();
            }
            private static final long serialVersionUID = 1L;
        };

        JButton addButton = new JButton(addBand);
        JButton removeButton = new JButton(removeBand);
        buttons.add(addButton);
        buttons.add(removeButton);

        group.add(buttons, BorderLayout.SOUTH);

        return group;
    }


    private JPanel getDefaultEditor(final AbioticFactor factor) {
        TableConfigPage<AbioticFactor> editor = new TableConfigPage<>(
                new AbioticFactor[] { factor },
                null,
                "Value");
        editor.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                refreshEditor();
            }
        });
        JPanel group = new JPanel(new BorderLayout());

        group.add(editor.getPanel());
        return group;
    }

    private class FactorNameRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof AbioticFactor) {
                setText(((AbioticFactor)value).getName());
            }
            return this;
        }
        private static final long serialVersionUID = 1L;
    }

    private class ActiveFactorRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof AbioticFactor) {
                setText(describeActiveFactor((AbioticFactor) value, index));
            }
            return this;
        }
        private static final long serialVersionUID = 1L;
    }

    private static String describeActiveFactor(AbioticFactor value, int index) {
        return "Factor " + (index + 1) + ": " + value.getName();
    }

    private Action addFactor = new AbstractAction("Add") {
        @Override
        public void actionPerformed(ActionEvent e) {
            AbioticFactor selectedValue = listOptions.getSelectedValue();
            if (selectedValue == null)
                return;

            modelSelected.addItem(selectedValue.copy());

            refresher.refreshConfig();
        }
        private static final long serialVersionUID = 1L;
    };

    private Action removeFactor = new AbstractAction("Remove") {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = listSelected.getSelectedIndex();
            if (index < 0)
                return;

            modelSelected.removeAt(index);

            refresher.refreshConfig();
        }
        private static final long serialVersionUID = 1L;
    };



    @Override
    public JPanel getPanel() {
        return myPanel;
    }

    @Override
    public void validateUI() throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

}
