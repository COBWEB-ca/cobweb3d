package cobweb3d.impl.logging;

import cobweb3d.plugins.mutators.DataLoggingMutator;

import java.io.File;
import java.util.Collection;

/**
 * Classes who implement this interface should have the ability to save data from DataTable to a file.
 */
public interface SavingStrategy {
    int save(DataTable coreData, Collection<DataLoggingMutator> plugins, File file);
}
