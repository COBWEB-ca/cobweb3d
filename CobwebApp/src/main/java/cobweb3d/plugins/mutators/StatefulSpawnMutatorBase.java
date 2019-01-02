package cobweb3d.plugins.mutators;

import cobweb3d.core.RandomSource;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.plugins.states.AgentState;
import cobwebutil.io.ParameterSerializable;

/**
 * Helper class for creating StatefulMutator that are also SpawnMutator.
 * Copies state from parent agent during asexual reproduction.
 * Picks random parent to copy state from during sexual reproduction.
 */
public abstract class StatefulSpawnMutatorBase<T extends AgentState, R extends ParameterSerializable> extends StatefulMutatorBase<T, R> implements SpawnMutator {

    private RandomSource rand;

    /**
     * @param stateClass class of state object
     * @param rand randomness source picking which parent to copy state from
     */
    protected StatefulSpawnMutatorBase(Class<T> stateClass, RandomSource rand) {
        super(stateClass);
        this.rand = rand;
    }

    /**
     * Creates state for newly spawned agent without parents
     * @param agent agent to create state for
     * @return state to store for agent, null if don't need state
     */
    protected abstract T stateForNewAgent(BaseAgent agent);

    /**
     * Creates state for newly spawned agent based on the parent's state
     * @param agent agent to create state for
     * @param parentState state of parent, could be null!
     * @return state to store for agent, null if don't need state
     */
    protected abstract T stateFromParent(BaseAgent agent, T parentState);

    @Override
    public void onSpawn(BaseAgent agent) {
        T state = stateForNewAgent(agent);
        if (state != null)
            setAgentState(agent, state);
    }

    @Override
    public void onSpawn(BaseAgent agent, BaseAgent parent) {
        T parentState = getAgentState(parent);

        T state = stateFromParent(agent, parentState);

        if (state != null)
            setAgentState(agent, state);
    }

    @Override
    public void onSpawn(BaseAgent agent, BaseAgent parent1, BaseAgent parent2) {
        // TODO: different probability than 50/50?
        BaseAgent parent = hasAgentState(parent2) && rand.getRandom().nextBoolean() ? parent2 : parent1;
        T parentState = getAgentState(parent);

        T state = stateFromParent(agent, parentState);
        if (state != null)
            setAgentState(agent, state);
    }

    @Override
    public void onDeath(BaseAgent agent) {
        // nothing
    }
}
