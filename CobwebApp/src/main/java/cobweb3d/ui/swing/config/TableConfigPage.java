package cobweb3d.ui.swing.config;

import cobweb3d.ui.swing.components.table.ConfigTableModel;
import cobweb3d.ui.swing.components.table.MixedValueJTable;
import cobwebutil.io.ChoiceCatalog;
import cobwebutil.io.ParameterSerializable;
import cobwebutil.swing.ColorLookup;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class TableConfigPage<T extends ParameterSerializable> implements ConfigPage {

    private final JPanel panel;
    private final MixedValueJTable paramTable;

    public TableConfigPage(T[] params, String name, String colPrefix) {
        this(params, name, colPrefix, null, null);
    }

    public TableConfigPage(T[] params, String name, ColorLookup agentColors) {
        this(params, name, null, agentColors, null);
    }

    public TableConfigPage(T[] params, String name, String colPrefix, ColorLookup agentColors) {
        this(params, name, colPrefix, agentColors, null);
    }

    public TableConfigPage(T[] params, String name, ColorLookup agentColors, ChoiceCatalog catalog) {
        this(params, name, null, agentColors, catalog);
    }

    public TableConfigPage(T[] params, String name, String colPrefix, ColorLookup agentColors, ChoiceCatalog catalog) {
        panel = new JPanel(new BorderLayout());

        if (colPrefix == null)
            colPrefix = "Agent";

        ConfigTableModel model = new ConfigTableModel(params, colPrefix);
        model.choiceCatalog = catalog;

        paramTable = new MixedValueJTable(model);
        TableColumnModel agParamColModel = paramTable.getColumnModel();
        agParamColModel.getColumn(0).setPreferredWidth(200);
        JScrollPane sp = new JScrollPane(paramTable);
        Util.makeGroupPanel(sp, name);

        if (agentColors != null)
            Util.colorHeaders(paramTable, true, agentColors);

        panel.add(sp, BorderLayout.CENTER);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    public void addTableModelListener(TableModelListener l) {
        paramTable.configModel.addTableModelListener(l);
    }

    @Override
    public void validateUI() throws IllegalArgumentException {
        Util.updateTable(paramTable);
    }
}
