package cobweb3d.plugins.mutators;

import cobweb3d.impl.logging.DataTable;
import cobweb3d.impl.stats.BaseStatsProvider;

import java.util.Collection;

public interface DataLoggingMutator extends AgentMutator {

    /**
     * classes implementing this interface should be able to return its name.
     *
     * @return the name of the data logger.
     */
    String getName();

    /**
     * classes implementing this interface should be able to return the number of tables that they have.
     *
     * @return the number of tables the data logger has.
     */
    int getTableCount();

    /**
     * By using methods from statsProvider, log the current simulation data into the data logger.
     *
     * @param statsProvider the statsProvider who monitors the simulation procedure.
     */
    void logData(BaseStatsProvider statsProvider);

    /**
     * classes implementing this interface should be able to return tables they have.
     *
     * @return all the tables that the data logger has.
     */
    Collection<DataTable> getTables();
}
