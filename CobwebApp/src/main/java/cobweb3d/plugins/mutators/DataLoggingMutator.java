package cobweb3d.plugins.mutators;

import cobweb3d.impl.logging.DataTable;
import cobweb3d.impl.stats.BaseStatsProvider;

import java.util.Collection;

public interface DataLoggingMutator extends AgentMutator {
    String getName();

    int getTableCount();

    void logData(BaseStatsProvider statsProvider);

    Collection<DataTable> getTables();
}
