package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.location.Location;

/**
 * Modifies agents when they move from one location to another.
 */
public interface StepMutator extends AgentMutator {

    /**
     * BaseAgent moved.
     *
     * @param agent BaseAgent in question.
     * @param from  Old Location. Null if agent is being placed in the environment
     * @param to    New location. Null if agent is being removed from the environment
     */
    void onStep(BaseAgent agent, Location from, Location to);
}
