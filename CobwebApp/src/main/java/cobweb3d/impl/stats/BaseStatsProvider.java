package cobweb3d.impl.stats;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.Simulation;
import cobweb3d.plugins.mutators.DataLoggingMutator;

import java.util.List;
import java.util.Set;

/**
 * This class collects data produced from a simulation.
 */
public class BaseStatsProvider {
    protected Simulation simulation;

    public BaseStatsProvider(Simulation simulation) {
        this.simulation = simulation;
    }

    /**
     * Count agents' total energy in current tick of the simulation.
     *
     * @return the total energy of all the current agents
     */
    public long countAgentEnergy() {
        long totalEnergy = 0;
        for (BaseAgent a : simulation.environment.getAgents()) totalEnergy += a.getEnergy();
        return totalEnergy;
    }

    public long getAgentCount() {
        return simulation.environment.getAgentCount();
    }

    /**
     * Count the number of agents which belong to the given type.
     *
     * @param agentType the type of agents that one is going to count
     * @return the number of agents which belong to the given type
     */
    public long countAgents(int agentType) {
        long count = 0;
        for (BaseAgent a : simulation.environment.getAgents()) if (a.getType() == agentType) count++;
        return count;
    }

    /**
     * Count the total energy of agents which belong to the given type.
     *
     * @param agentType the type of agents that one is going to count
     * @return the total energy of agents which belong to the given type
     */
    public long countAgentEnergy(int agentType) {
        long totalEnergy = 0;
        for (BaseAgent a : simulation.environment.getAgents())
            if (a.getType() == agentType) totalEnergy += a.getEnergy();
        return totalEnergy;
    }

    public int getAgentTypeCount() {
        return simulation.getAgentTypeCount();
    }

    public long getTime() {
        return simulation.getTime();
    }

    public Set<DataLoggingMutator> getDataLoggingPlugins() {
        return simulation.mutatorListener.getDataLoggingMutators();
    }

    public List<BaseAgent> getAgents() {
        return simulation.getAgents();
    }
}
