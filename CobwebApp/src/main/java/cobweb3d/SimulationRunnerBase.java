package cobweb3d;

import cobweb3d.impl.Simulation;
import cobweb3d.impl.SimulationConfig;
import cobweb3d.impl.logging.AutoSavingLogManager;
import cobweb3d.impl.logging.LogConfig;
import cobweb3d.impl.logging.LogManager;
import cobweb3d.ui.UpdatableUI;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class SimulationRunnerBase implements SimulationRunner, SimulationRunner.LoggingSimulationRunner {

    protected Simulation simulation;

    protected volatile boolean running = false;

    private long tickAutoStop = 0;

    private LogManager logManager = null;
    private Set<UpdatableUI> uiComponents = new HashSet<>();

    public SimulationRunnerBase(Simulation simulation) {
        this.simulation = simulation;
    }

    public void loadSimulation(SimulationConfig simulationConfig) {
        simulation.load(simulationConfig);
        initializeLogManager();
        setLogConfig(simulationConfig.logConfig);
    }

    @Override
    public void step() {
        if (isRunning()) {
            stop();
        } else {
            simulation.step();
            updateUI(true);
        }
    }

    @Override
    public void run() {
        if (running)
            throw new IllegalStateException("Already running!");

        running = true;
        notifyStarted();

        System.out.println(String.format(
				"Running '%1$s' for %2$d steps. Log: %3$s",
				simulation.simulationConfig.fileName,
				getAutoStopTime(),
                logManager == null ? "No" : "Yes"));

        long increment = getAutoStopTime() / 100;
        if (increment > 1000)
            increment = 1000;
        if (increment == 0)
            increment = 1000;

        while (isRunning()) {
            simulation.step();
            updateUI(true);

            long time = getSimulation().getTime();
            if (time % increment == 0) {
                if (getAutoStopTime() != 0) {
                    System.out.println(String.format(
                            "Step: %1$d / %2$d (%3$d%%)",
                            time,
                            getAutoStopTime(),
                            100 * time / getAutoStopTime()
                    ));
                } else {
                    System.out.println(String.format(
                            "Step: %1$d",
                            time
                    ));
                }
            }

            // Stop at target time
            if (getAutoStopTime() != 0 && simulation.getTime() >= getAutoStopTime()) {
                stop();
            }
        }
        System.out.println("Done!");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void stop() {
        running = false;
        notifyStopped();
    }

    @Override
    public void reset() {
        stop();
        simulation.resetTime();
        clearLogManager();
        loadSimulation(simulation.simulationConfig);
        for (UpdatableUI updatableUI : new LinkedList<>(uiComponents)) {
            updatableUI.update(false);
        }
    }

    protected void notifyStarted() {
        for (UpdatableUI updatableUI : new LinkedList<>(uiComponents)) {
            updatableUI.onStarted();
        }
    }

    protected void notifyStopped() {
        for (UpdatableUI updatableUI : new LinkedList<>(uiComponents)) {
            updatableUI.onStopped();
        }
    }

    public void setLogConfig(LogConfig logConfig) {
        logManager.updateLogConfig(logConfig);
    }

    public void clearLogManager() {
        disableLogging();
        logManager = null;//new LogManager(simulation);
    }

    public void disableLogging() {
        if (logManager != null) {
            if (logManager instanceof AutoSavingLogManager) ((AutoSavingLogManager) logManager).saveLog();
            removeUIComponent(logManager);
            for (UpdatableUI loggingUI : uiComponents) {
                if (loggingUI instanceof UpdatableUI.UpdateableLoggingUI) {
                    ((UpdatableUI.UpdateableLoggingUI) loggingUI).onLogStopped();
                }
            }
        }
    }

    public void enableLogging() {
        if (logManager == null) initializeLogManager();
        if (!uiComponents.contains(logManager)) uiComponents.add(logManager);
        for (UpdatableUI loggingUI : uiComponents) {
            if (loggingUI instanceof UpdatableUI.UpdateableLoggingUI) {
                ((UpdatableUI.UpdateableLoggingUI) loggingUI).onLogStarted();
            }
        }
    }

    public void initializeLogManager() {
        disableLogging();
        logManager = new LogManager(simulation);
    }

    public void setLogManagerAutoSaveFile(File file) {
        if (logManager != null) {
            if (logManager instanceof AutoSavingLogManager) ((AutoSavingLogManager) logManager).saveLog();
            removeUIComponent(logManager);
            for (UpdatableUI loggingUI : uiComponents) {
                if (loggingUI instanceof UpdatableUI.UpdateableLoggingUI) {
                    ((UpdatableUI.UpdateableLoggingUI) loggingUI).onLogStopped();
                }
            }
            logManager = new AutoSavingLogManager(logManager, file);
        } else {
            logManager = new AutoSavingLogManager(simulation, file);
        }
        uiComponents.add(logManager);
        for (UpdatableUI loggingUI : uiComponents) {
            if (loggingUI instanceof UpdatableUI.UpdateableLoggingUI) {
                ((UpdatableUI.UpdateableLoggingUI) loggingUI).onLogStarted();
            }
        }
    }

    public void saveLogManager(File file) {
        if (logManager != null) {
            logManager.saveLog(file);
        } else {
            System.err.println("Did not save log: logManager is null.");
        }
    }

    /* TODO
    /**
	 * Writes simulation report to writer.
	 * @param writer where to write report.
	 * @see AgentReporter

	public void report(Writer writer) {
		if (simulation != null) {
			try {
				AgentReporter.report(writer, simulation);
				writer.flush();
				writer.close();
			} catch (IOException ex) {
				throw new UserInputException("Cannot save report file", ex);
			}
		}
	}
    */

    protected void updateUI(boolean synchronous) {
        for (UpdatableUI client : new LinkedList<UpdatableUI>(uiComponents)) {
            if (synchronous || client.isReadyToUpdate()) {
                client.update(synchronous);
            }
        }
    }

    @Override
    public void addUIComponent(UpdatableUI ui) {
        uiComponents.add(ui);
        ui.update(true);
    }

    @Override
    public void removeUIComponent(UpdatableUI ui) {
        uiComponents.remove(ui);
    }

    @Override
    public Simulation getSimulation() {
        return simulation;
    }

    public long getAutoStopTime() {
        return tickAutoStop;
    }

    @Override
    public void setAutoStopTime(long t) {
        tickAutoStop = t;
    }

    @Override
    public boolean isLogging() {
        return logManager != null && uiComponents.contains(logManager);//statsLogger != null;
    }

    @Override
    public String getLoggingStatus() {
        return logManager != null ? logManager.getLoggingStatus() : "Not Logging";
    }
}
