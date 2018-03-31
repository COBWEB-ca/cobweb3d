package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;

/**
 * Modifies agents when one makes contact with another.
 */
public interface ContactMutator extends AgentMutator {

    /**
     * Event called when an agent makes contact with another.
     *
     * @param bumper BaseAgent that moved to make contact.
     * @param bumpee BaseAgent that got bumped into by the other.
     */
    void onContact(BaseAgent bumper, BaseAgent bumpee);
}
