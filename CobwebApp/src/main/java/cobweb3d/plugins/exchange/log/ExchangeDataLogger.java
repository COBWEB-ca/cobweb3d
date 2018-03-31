package cobweb3d.plugins.exchange.log;

import cobweb3d.impl.logging.DataTable;
import cobweb3d.impl.stats.BaseStatsProvider;
import cobweb3d.plugins.exchange.ExchangeParams;
import cobweb3d.plugins.mutators.DataLoggingMutator;
import cobweb3d.plugins.states.AgentState;

import java.util.Collection;
import java.util.Collections;

public class ExchangeDataLogger implements DataLoggingMutator {
    public static final int FIRST_DATA_ROW = 0;
    int nextDataRow = 0;

    DataTable dataTable;

    ExchangeParams params;

    public ExchangeDataLogger(ExchangeParams params) {
        this.params = params;
        initializeTable();
    }

    @Override
    public String getName() {
        return "Exchange Data";
    }

    @Override
    public void logData(BaseStatsProvider statsProvider) {
        if (dataTable == null) return;
        DataTable.SmartLogRow row = dataTable.getRow((int) statsProvider.getTime() - 1); // TODO: keep/sync nextDataRow somehow?
        row.putVal(0, statsProvider.getTime());

        long totAgentCount = statsProvider.getAgentCount();

        float totalX = ExchangeStatTracker.getTotalX(statsProvider);
        float totalY = ExchangeStatTracker.getTotalY(statsProvider);
        float totalU = ExchangeStatTracker.getTotalUtility(statsProvider, params);

        row.putVal(1, totAgentCount);
        row.putVal(2, totalX);
        row.putVal(3, totalY);
        row.putVal(4, totalU);
        row.putVal(5, (totalU / (float) totAgentCount));
        int agentCount = params.agentParams.length;
        for (int i = 0; i < agentCount; i++) {
            long typeAgentCount = statsProvider.countAgents(i);
            float typeU = ExchangeStatTracker.getUtilityForAgent(statsProvider, params, i);
            row.putVal((i * 4) + 6, (typeU / (float) typeAgentCount));
            row.putVal((i * 4) + 7, typeU);
            row.putVal((i * 4) + 8, ExchangeStatTracker.getXForAgent(statsProvider, params, i));
            row.putVal((i * 4) + 9, ExchangeStatTracker.getYForAgent(statsProvider, params, i));
        }
        nextDataRow++;
    }

    @Override
    public <T extends AgentState> boolean acceptsState(Class<T> type, T value) {
        return false;
    }

    private void initializeTable() {
        dataTable = new DataTable("Tick", "Total Agents", "Total X", "Total Y", "Total Utility", "Average Utility");
        int agentCount = params.agentParams.length;
        for (int i = 1; i <= agentCount; i++) {
            dataTable.addColumn("Agent " + i + " Average Utility");
            dataTable.addColumn("Agent " + i + " Total Utility");
            dataTable.addColumn("Agent " + i + " Total X");
            dataTable.addColumn("Agent " + i + " Total Y");
        }
        nextDataRow = FIRST_DATA_ROW;
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
