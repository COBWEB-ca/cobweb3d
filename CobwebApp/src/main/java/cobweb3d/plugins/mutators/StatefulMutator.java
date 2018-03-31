package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.plugins.states.AgentState;

/**
 * Mutator that stores state on a per-agent basis
 *
 * @param <T> Class of state object used
 */
public interface StatefulMutator<T extends AgentState> extends AgentMutator {

    /**
     * Gets state of agent associated with this mutator.
     *
     * @param agent agent to get state for
     * @return state object for agent, null if state not stored.
     */
    T getAgentState(BaseAgent agent);

    /**
     * Does the mutator have a state for this agent?
     *
     * @param agent agent
     * @return agent state, null when no state
     */
    boolean hasAgentState(BaseAgent agent);

    /**
     * Returns the Class of the state object
     *
     * @return class that can be used when generic types are erased
     */
    Class<T> getStateClass();
}
