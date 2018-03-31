package cobweb3d.impl.logging;

import cobweb3d.plugins.mutators.DataLoggingMutator;

import java.io.File;
import java.util.Collection;

public interface SavingStrategy {
    int save(DataTable coreData, Collection<DataLoggingMutator> plugins, File file);
}
