package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.states.AgentState;
import cobwebutil.io.ParameterSerializable;

/**
 * Helper base class for creating StatefulMutators.
 * Handles storage of state.
 */
public abstract class StatefulMutatorBase<T extends AgentState, R extends ParameterSerializable> implements StatefulMutator<T>, ConfiguratedMutator<R> {

    private final Class<T> stateClass;

    protected StatefulMutatorBase(Class<T> stateClass) {
        this.stateClass = stateClass;
    }

    @Override
    public T getAgentState(BaseAgent agent) {
        if (agent instanceof Agent) return ((Agent) agent).getState(getStateClass());
        else return null;
    }

    @Override
    public boolean hasAgentState(BaseAgent agent) {
        return getAgentState(agent) != null;
    }

    protected T removeAgentState(BaseAgent agent) {
        if (agent instanceof Agent) return ((Agent) agent).removeState(getStateClass());
        else return null;
    }

    protected void setAgentState(BaseAgent agent, T state) {
        if (agent instanceof Agent) ((Agent) agent).setState(getStateClass(), state);
    }

    @Override
    public Class<T> getStateClass() {
        return stateClass;
    }

    /**
     * Checks whether the given AgentState is valid for this mutator in the current simulation configuration
     */
    protected abstract boolean validState(T value);

    @SuppressWarnings("unchecked")
    @Override
    public <U extends AgentState> boolean acceptsState(Class<U> type, U value) {
        if (stateClass.equals(type)) {
            return validState((T) value);
        }
        return false;
    }
}
