package cobweb3d.impl.logging;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This is a 2D table.
 */
public class DataTable {
    // Given a integer, rowMap returns the row whose index is the integer.
    public SortedMap<Integer, SmartLogRow> rowMap;
    // columnInts represents the column names of the DataTable.
    public SortedMap<Integer, String> columnInts;

    public DataTable(String... columnNames) {
        rowMap = new TreeMap<>();
        columnInts = new TreeMap<>();
        addColumns(columnNames);
    }

    /**
     * Add more columns to the DataTable
     *
     * @param columnNames the names of columns which are going to be added
     */
    public void addColumns(String... columnNames) {
        int prevSize = columnInts.size();
        for (int i = 0; i < columnNames.length; i++) {
            if (!columnInts.containsValue(columnNames[i])) this.columnInts.put(i + prevSize, columnNames[i]);
        }
    }

    /**
     * Add one more column to the DataTable
     *
     * @param columnName the name of the column which is going to be added
     */
    public void addColumn(String columnName) {
        if (!columnInts.containsValue(columnName)) this.columnInts.put(columnInts.size(), columnName);
    }

    /**
     * Return the selected row of the DataTable.
     * If the row doesn't exist, it will initialize an empty row based on that index and return it.
     *
     * @param tick the index of row which is selected.
     * @return the row corresponding to the index.
     */
    public SmartLogRow getRow(int tick) {
        if (rowMap.containsKey(tick)) return rowMap.get(tick);
        else {
            SmartLogRow row = new SmartLogRow(null);
            rowMap.put(tick, row);
            return row;
        }
    }

    /**
     * This class represents a row of the log table.
     */
    public class SmartLogRow {
        public Map<String, Integer> columns;
        public Map<Integer, Cell> cells = new TreeMap<>();

        public SmartLogRow(Map<String, Integer> columns) {
            this.columns = columns;
        }

        public void putVal(String column, Object value) {
            if (!cells.containsKey(column)) cells.put(this.columns.get(column), new Cell(value));
            else {
                Cell cell = cells.get(column);
                cell.value = value;
            }
        }

        /**
         * It lets the content of the cell selected by columnIndex to be the given value.
         * Note: If the cell selected by columnIndex has already mapped to a value, that value will be overwritten.
         *
         * @param columnIndex the index of the selected cell
         * @param value the value which will be put into that cell
         */
        public void putVal(int columnIndex, Object value) {
            if (!cells.containsKey(columnIndex)) cells.put(columnIndex, new Cell(value));
            else {
                Cell cell = cells.get(columnIndex);
                cell.value = value;
            }
        }

        /**
         * A container which stores one object.
         */
        public class Cell {
            public Object value;

            public Cell(Object value) {
                this.value = value;
            }
        }
    }
}
