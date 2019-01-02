package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;

public interface MoveMutator extends AgentMutator {
    /*
     * Override the default move. Returns true if it actually does (in case if there is a probability that
     * a move will be overridden). false otherwise.
     */
    boolean overrideMove(BaseAgent agent);
}
