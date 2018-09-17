package cobweb3d.impl.logging.strategies.printwriter;

import cobweb3d.impl.logging.DataTable;
import cobweb3d.plugins.mutators.DataLoggingMutator;

import java.io.PrintWriter;
import java.util.*;

/**
 * This class saves data in CSV form.
 */
public class CSVSavingStrategy extends AbstractPrintWritingSavingStrategy {

    private char delimiter;

    public CSVSavingStrategy() {
        this(',');
    }

    public CSVSavingStrategy(char delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * This method saves data in DataTable to PrintWriter.
     *
     * @param printWriter An IO stream where data is written into.
     * @param coreData An instance of class DataTable which comprises data of a simulation.
     * @param plugins A collection of plugins which provide the name of each column.
     * @param lastSavedTick From which line do we begin to write data into the printWriter.
     * @return The last row number of the current DataTable.
     */
    @Override
    int saveDataToPrintWriter(PrintWriter printWriter, DataTable coreData, Collection<DataLoggingMutator> plugins, int lastSavedTick) {
        // First it verifies whether or not we write entire DataTable into the file.
        // If so, it uses plugins given to initialize column names.
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

    /**
     * Write the input row into the PrintWriter.
     * Note: It's not necessarily in CSV form, since the delimiter can be changed.
     *
     * @param printWriter The place to write in.
     * @param row A row of data which is going to be written into the file.
     */
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
