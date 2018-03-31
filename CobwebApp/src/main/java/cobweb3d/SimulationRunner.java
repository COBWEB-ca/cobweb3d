package cobweb3d;


import cobweb3d.ui.SimulationInterface;
import cobweb3d.ui.UpdatableUI;

/**
 * Continuously steps through simulation and updates UpdatableUI clients at every step.
 */
public interface SimulationRunner {

    /**
     * Perform one simulation step
     */
    void step();

    /**
     * Start stepping through the simulation
     */
    void run();

    /**
     * Is the SimulationRunnerBase running the simulation?
     */
    boolean isRunning();

    /**
     * Stop stepping through the simulation
     */
    void stop();

    void reset();

    /**
     * Simulation that is being run
     */
    SimulationInterface getSimulation();

    void removeUIComponent(UpdatableUI ui);

    void addUIComponent(UpdatableUI ui);

    void setAutoStopTime(long time);

    interface LoggingSimulationRunner {
        boolean isLogging();

        default String getLoggingStatus() {
            return "Logging";
        }
    }
}
