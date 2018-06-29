package cobweb3d.impl.logging.strategies.printwriter;

import cobweb3d.impl.logging.DataTable;
import cobweb3d.impl.logging.SavingStrategy;
import cobweb3d.plugins.mutators.DataLoggingMutator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collection;

public abstract class AbstractPrintWritingSavingStrategy implements SavingStrategy {

    private File lastFile;
    private int lastSavedTick = 0;

    /**
     * A wrapper method for saveDataToPrintWriter().
     * This method determines whether or not the program is writing into a new file,
     * and opens IO stream, which is passed into saveDataToPrintWriter().
     *
     * @param coreData An instance of class DataTable which comprises data of a simulation.
     * @param plugins A collection of plugins which provide the name of each column.
     * @param file The file where data would be written in.
     * @return The last tick of data that has already been saved.
     */
    @Override
    public int save(DataTable coreData, Collection<DataLoggingMutator> plugins, File file) {
        if (file == null) return 0;
        // If the last file doesn't exist or is different from current file, we will rewrite coreData into the new file.
        if (lastFile == null || (!lastFile.equals(file) && !lastFile.getAbsolutePath().equals(file.getAbsolutePath())))
            lastSavedTick = 0;
        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
            // plugins are used to provide column names only if we are writing into a new file.
            this.lastSavedTick = saveDataToPrintWriter(printWriter, coreData, plugins, this.lastSavedTick);
            printWriter.flush();
            printWriter.close();
        } catch (Exception ex) {
            System.err.println("Failed saving log to " + file.getAbsolutePath());
        }
        lastFile = file;
        return this.lastSavedTick;
    }

    abstract int saveDataToPrintWriter(PrintWriter printWriter, DataTable coreData, Collection<DataLoggingMutator> plugins,
                                       int lastSavedTick);
}
