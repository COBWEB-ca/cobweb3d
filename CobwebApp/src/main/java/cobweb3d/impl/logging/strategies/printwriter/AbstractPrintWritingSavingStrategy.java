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

    @Override
    public int save(DataTable coreData, Collection<DataLoggingMutator> plugins, File file) {
        if (file == null) return 0;
        if (lastFile == null || (!lastFile.equals(file) && !lastFile.getAbsolutePath().equals(file.getAbsolutePath())))
            lastSavedTick = 0;
        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
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
