package cobweb3d.plugins.transform.log;

import cobweb3d.impl.logging.DataTable;
import cobweb3d.impl.stats.BaseStatsProvider;
import cobweb3d.plugins.mutators.DataLoggingMutator;
import cobweb3d.plugins.states.AgentState;

import java.util.Collection;
import java.util.Collections;

public class TransformationDataLogger implements DataLoggingMutator {
    DataTable dataTable;

    public TransformationDataLogger(int agentTypeCount) {
        initializeTable(agentTypeCount);
    }

    @Override
    public String getName() {
        return "Transformation Data";
    }

    @Override
    public void logData(BaseStatsProvider statsProvider) {
        if (dataTable == null) return;
        DataTable.SmartLogRow row = dataTable.getRow((int) statsProvider.getTime()); // TODO: keep/sync nextDataRow somehow?
        row.putVal(0, statsProvider.getTime());

        //row.putVal(1, TransformationStatTracker.getAverageSpeed(statsProvider));
        int agentCount = statsProvider.getAgentTypeCount();
        for (int i = 0; i < agentCount; i++) {
            //       row.putVal(i + 2, TransformationStatTracker.getAverageSpeedForAgent(statsProvider, i));
        }
    }

    @Override
    public <T extends AgentState> boolean acceptsState(Class<T> type, T value) {
        return false;
    }

    private void initializeTable(int agentTypeCount) {
        dataTable = new DataTable("Tick", "Total Average Transformation Speed");
        int agentCount = agentTypeCount;
        for (int i = 1; i <= agentCount; i++) {
            dataTable.addColumn("Agent " + i + " Average Transformation Speed");
        }
    }

    @Override
    public int getTableCount() {
        return 1;
    }

    @Override
    public Collection<DataTable> getTables() {
        return Collections.singleton(dataTable);
    }
}
