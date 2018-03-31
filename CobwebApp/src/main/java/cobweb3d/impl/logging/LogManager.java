package cobweb3d.impl.logging;

import cobweb3d.impl.Simulation;
import cobweb3d.impl.logging.strategies.ExcelXSSFSavingStrategy;
import cobweb3d.impl.logging.strategies.printwriter.CSVSavingStrategy;
import cobweb3d.impl.logging.strategies.printwriter.PlainTextSavingStategy;
import cobweb3d.impl.stats.BaseStatsProvider;
import cobweb3d.plugins.mutators.DataLoggingMutator;
import cobweb3d.ui.UpdatableUI;
import cobwebutil.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class LogManager implements UpdatableUI {
    private DataTable coreDataTable;
    private BaseStatsProvider baseStatsProvider;
    private int nextDataRow = 0;

    private LogConfig logConfig;

    private Set<DataLoggingMutator> dataLoggingMutators;
    private Set<DataLoggingMutator> enabledMutators;

    public LogManager(@NotNull LogManager prev) {
        coreDataTable = prev.coreDataTable;
        baseStatsProvider = prev.baseStatsProvider;
        nextDataRow = prev.nextDataRow;
        logConfig = prev.logConfig;
        writeHeaders();
    }

    public LogManager(@NotNull Simulation simulation) {
        coreDataTable = new DataTable();
        baseStatsProvider = new BaseStatsProvider(simulation);
        logConfig = simulation.simulationConfig.logConfig;
        writeHeaders();
    }

    public Set<DataLoggingMutator> getDataLoggingPlugins() {
        return baseStatsProvider.getDataLoggingPlugins();
    }

    public Set<DataLoggingMutator> getEnabledMutators() {
        if (enabledMutators == null || dataLoggingMutators.size() != baseStatsProvider.getDataLoggingPlugins().size()) {
            dataLoggingMutators = baseStatsProvider.getDataLoggingPlugins();
            enabledMutators = new HashSet<>();
            for (DataLoggingMutator plugin : getDataLoggingPlugins()) {
                if (logConfig.enabled(plugin.getName())) enabledMutators.add(plugin);
            }
        }
        return enabledMutators;
    }

    public static SavingStrategy getSavingStrategyForExt(String fileExt) {
        if (fileExt != null && !fileExt.isEmpty()) {
            String lowercaseExt = fileExt.toLowerCase();
            if (lowercaseExt.contains("csv"))
                return new CSVSavingStrategy();
            else if (lowercaseExt.contains("log"))
                return new PlainTextSavingStategy();
            else if (lowercaseExt.contains("xlsx"))
                return new ExcelXSSFSavingStrategy();
        }

        // Last Resort is CSV
        return new CSVSavingStrategy();
    }

    public void writeLogEntry(long tick, DataTable.SmartLogRow logRow) {
        long agentCount = baseStatsProvider.getAgentCount();
        long totEnergy = baseStatsProvider.countAgentEnergy();

        logRow.putVal(0, tick);
        logRow.putVal(1, agentCount);
        logRow.putVal(2, totEnergy);
        logRow.putVal(3, ((float) totEnergy / (float) agentCount));

        for (int i = 0; i < baseStatsProvider.getAgentTypeCount(); i++) {
            long i_agentCount = baseStatsProvider.countAgents(i),
                    i_totEnergy = baseStatsProvider.countAgentEnergy(i);
            logRow.putVal(4 + (i * 3), i_agentCount);
            logRow.putVal(5 + (i * 3), i_totEnergy);
            logRow.putVal(6 + (i * 3), ((float) i_totEnergy / (float) i_agentCount));
        }
    }

    @Override
    public void update(boolean synchronous) {
        if (logConfig == null) {
            writeLogEntry(baseStatsProvider.getTime(), coreDataTable.getRow(nextDataRow));
            for (DataLoggingMutator plugin : getDataLoggingPlugins()) {
                plugin.logData(baseStatsProvider);
            }
        } else {
            if (logConfig.logCore) writeLogEntry(baseStatsProvider.getTime(), coreDataTable.getRow(nextDataRow));
            for (DataLoggingMutator plugin : getEnabledMutators()) {
                plugin.logData(baseStatsProvider);
            }
        }
        nextDataRow++;
    }

    public void saveLog(@NotNull File file, @NotNull SavingStrategy savingStrategy) {
        if (savingStrategy != null)
            savingStrategy.save(logConfig == null || logConfig.logCore ? coreDataTable : null, logConfig == null ? getDataLoggingPlugins() : getEnabledMutators(), file);
        else System.err.println("Did not save log: null SavingStategy in LogManager.saveLog()!");
    }

    public void writeHeaders() {
        coreDataTable.addColumns("Tick", "Total Agent Count", "Total Agent Energy", "Average Agent Energy");
        for (int i = 0; i < baseStatsProvider.getAgentTypeCount(); i++) {
            coreDataTable.addColumn("Agent " + (i + 1) + " Count");
            coreDataTable.addColumn("Total Agent " + (i + 1) + " Energy");
            coreDataTable.addColumn("Average Agent " + (i + 1) + " Energy");
        }
        /* TODO ?
        for (DataLoggingMutator plugin : getDataLoggingPlugins()) {
            plugin.getTableCount();
        }
        */
    }

    public String getLoggingStatus() {
        return "Logging to Memory";
    }

    // Updates EVERY frame even when update speed is so fast its async.
    @Override
    public boolean isReadyToUpdate() {
        return true;
    }

    public void saveLog(@NotNull File file) {
        saveLog(file, getSavingStrategyForExt(FileUtils.getFileExtension(file)));
    }

    public void updateLogConfig(LogConfig newLogConfig) {
        this.logConfig = newLogConfig;
        this.enabledMutators = null; // Invalidate;
    }
}
