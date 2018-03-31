package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;

/**
 * Modifies agents when they are born.
 * Note: the agent's location is initialized AFTER spawn and removed BEFORE death!
 * Use StepMutator if you want initial and final locations
 */
public interface SpawnMutator extends AgentMutator {

    /**
     * BaseAgent died.
     *
     * @param agent BaseAgent that died.
     */
    default void onDeath(BaseAgent agent) {
    }

    /**
     * BaseAgent spawned by user.
     *
     * @param agent BaseAgent spawned.
     */
    default void onSpawn(BaseAgent agent) {
    }

    /**
     * BaseAgent produced asexually
     *
     * @param agent  BaseAgent produced.
     * @param parent Asexual parent.
     */
    default void onSpawn(BaseAgent agent, BaseAgent parent) {
        onSpawn(agent);
    }

    /**
     * BaseAgent produced sexually.
     *
     * @param agent   BaseAgent produced.
     * @param parent1 First parent.
     * @param parent2 Second parent.
     */
    default void onSpawn(BaseAgent agent, BaseAgent parent1, BaseAgent parent2) {
        onSpawn(agent);
    }
}
