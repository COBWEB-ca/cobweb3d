package cobweb3d.ui.swing.config;

import cobwebutil.swing.ColorLookup;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class Util {

    public static void colorHeaders(JTable ft, boolean skipFirst, ColorLookup colorMap) {
        int firstCol = skipFirst ? 1 : 0;

        int count = ft.getColumnCount() - firstCol;
        for (int col = 0; col < count; col++) {
            DefaultTableCellRenderer r = new DefaultTableCellRenderer();
            if (colorMap != null) r.setBackground(colorMap.getColor(col, count));
            ft.getColumnModel().getColumn(col + firstCol).setHeaderRenderer(r);
            LookAndFeel.installBorder(ft.getTableHeader(), "TableHeader.cellBorder");
        }
    }

    public static void makeGroupPanel(JComponent target, String title) {
        target.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.blue), title));
    }

    public static void updateTable(JTable table) {
        int row = table.getEditingRow();
        int col = table.getEditingColumn();
        if (table.isEditing()) {
            table.getCellEditor(row, col).stopCellEditing();
        }
    }
}
