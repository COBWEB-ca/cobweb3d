package cobweb3d.impl.logging.strategies.printwriter;

import cobweb3d.impl.logging.DataTable;
import cobweb3d.plugins.mutators.DataLoggingMutator;

import java.io.PrintWriter;
import java.util.*;

public class CSVSavingStrategy extends AbstractPrintWritingSavingStrategy {

    private char delimiter;

    public CSVSavingStrategy() {
        this(',');
    }

    public CSVSavingStrategy(char delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    int saveDataToPrintWriter(PrintWriter printWriter, DataTable coreData, Collection<DataLoggingMutator> plugins, int lastSavedTick) {
        if (lastSavedTick == 0) {
            Iterator<Map.Entry<Integer, String>> iterator = coreData.columnInts.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> pairs = iterator.next();
                printWriter.append(pairs.getValue());
                printWriter.append(delimiter);
            }
            for (DataLoggingMutator plugin : plugins) {
                for (DataTable dataTable : plugin.getTables()) {
                    iterator = dataTable.columnInts.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Integer, String> pairs = iterator.next();
                        printWriter.append(pairs.getValue());
                        if (iterator.hasNext()) printWriter.append(delimiter);
                    }
                }
            }
            printWriter.append("\r\n");
        }

        int maxRows = coreData.rowMap.size();
        for (DataLoggingMutator plugin : plugins) {
            for (DataTable dataTable : plugin.getTables()) {
                int count = dataTable.rowMap.size();
                if (count > maxRows) maxRows = count;
            }
        }
        for (int r = lastSavedTick; r < maxRows; r++) {
            printRowToCSV(printWriter, coreData.rowMap.get(r));
            printWriter.append(delimiter);

            Iterator<DataLoggingMutator> dataLoggingMutatorIterator = plugins.iterator();
            while (dataLoggingMutatorIterator.hasNext()) {
                Iterator<DataTable> tableIterator = dataLoggingMutatorIterator.next().getTables().iterator();
                while (tableIterator.hasNext()) {
                    printRowToCSV(printWriter, tableIterator.next().rowMap.get(r));
                    if (tableIterator.hasNext() || dataLoggingMutatorIterator.hasNext()) printWriter.append(',');
                }
            }

            printWriter.append("\r\n");
        }
        return maxRows;
    }

    public void printRowToCSV(PrintWriter printWriter, DataTable.SmartLogRow row) {
        if (row == null) return;
        Set<Integer> col = new TreeSet<>(row.cells.keySet());
        int lastCol = 0;
        for (int i : col) {
            for (int j = 0; j < i - lastCol; j++)
                printWriter.append(delimiter);
            printWriter.append(row.cells.get(i).value.toString());
            lastCol = i;
        }
    }
}
