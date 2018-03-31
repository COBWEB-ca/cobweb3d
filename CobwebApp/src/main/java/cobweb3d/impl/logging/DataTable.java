package cobweb3d.impl.logging;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class DataTable {
    public SortedMap<Integer, SmartLogRow> rowMap;
    public SortedMap<Integer, String> columnInts;

    public DataTable(String... columnNames) {
        rowMap = new TreeMap<>();
        columnInts = new TreeMap<>();
        addColumns(columnNames);
    }

    public void addColumns(String... columnNames) {
        int prevSize = columnInts.size();
        for (int i = 0; i < columnNames.length; i++) {
            if (!columnInts.containsValue(columnNames[i])) this.columnInts.put(i + prevSize, columnNames[i]);
        }
    }

    public void addColumn(String columnName) {
        if (!columnInts.containsValue(columnName)) this.columnInts.put(columnInts.size(), columnName);
    }

    public SmartLogRow getRow(int tick) {
        if (rowMap.containsKey(tick)) return rowMap.get(tick);
        else {
            SmartLogRow row = new SmartLogRow(null);
            rowMap.put(tick, row);
            return row;
        }
    }

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

        public void putVal(int columnIndex, Object value) {
            if (!cells.containsKey(columnIndex)) cells.put(columnIndex, new Cell(value));
            else {
                Cell cell = cells.get(columnIndex);
                cell.value = value;
            }
        }

        public class Cell {
            public Object value;

            public Cell(Object value) {
                this.value = value;
            }
        }
    }
}
