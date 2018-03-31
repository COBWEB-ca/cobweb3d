package cobweb3d.ui.swing.config.logging;

import cobweb3d.impl.logging.LogConfig;
import cobweb3d.ui.swing.components.table.MixedValueJTable;
import cobweb3d.ui.swing.config.ConfigPage;
import cobweb3d.ui.swing.config.Util;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class LogConfigPage implements ConfigPage {
    private JPanel mainPanel;

    private LogConfig params;

    public LogConfigPage(LogConfig params) {
        this.params = params;
        mainPanel = makePanel();
    }

    private JPanel makePanel() {
        JPanel jPanel = new JPanel(new BorderLayout());

        TableModel tableModel = new AbstractTableModel() {
            String[] pluginEntryKeys = params.editableEntries.toArray(new String[params.editableEntries.size()]);

            @Override
            public int getRowCount() {
                return pluginEntryKeys.length + 1;
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == 0) {
                    if (rowIndex == 0) return "Core Data";
                    else return pluginEntryKeys[rowIndex - 1];
                } else {
                    if (rowIndex == 0)
                        return params.logCore;//return dataLoggingMutators.toArray()[columnIndex - 1].getClass().getSimpleName();
                    else
                        return params.enabled(pluginEntryKeys[rowIndex - 1]);// return params.enabled((Class<DataLoggingMutator>) dataLoggingMutators.toArray()[columnIndex - 1].getClass());
                }
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex > 0;
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (columnIndex != 0) {
                    if (rowIndex == 0) {
                        params.logCore = (boolean) aValue;
                    } else {
                        if ((boolean) aValue) {
                            params.enableMutator(pluginEntryKeys[rowIndex - 1]);
                        } else {
                            params.disableMutator(pluginEntryKeys[rowIndex - 1]);
                        }
                    }
                }
                super.setValueAt(aValue, rowIndex, columnIndex);
            }

            @Override
            public String getColumnName(int column) {
                if (column == 0) return "Plugin";
                else if (column == 1) return "Enabled";
                return super.getColumnName(column);
            }
        };
        JTable jTable = new MixedValueJTable(tableModel);
        jTable.getColumn("Plugin").setPreferredWidth(200);
        Util.colorHeaders(jTable, false, null);
        jPanel.add(new JScrollPane(jTable));
        Util.makeGroupPanel(jPanel, "Logging");
        return jPanel;
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public void validateUI() throws IllegalArgumentException {
        // Nothing
    }
}
