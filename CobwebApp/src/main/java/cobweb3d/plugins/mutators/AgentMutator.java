package cobweb3d.plugins.mutators;

import cobweb3d.plugins.states.AgentState;

/**
 * Modifies agents' parameters during the simulation.
 */
public interface AgentMutator {

    /**
     * Checks whether this mutator can use given AgentState in the current simulation configuration.
     */
    <T extends AgentState> boolean acceptsState(Class<T> type, T value);
}
