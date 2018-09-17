package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;

public interface UpdateMutator extends AgentMutator {

    /**
     * One base agent is going to update its state.
     *
     * @param agent the agent to update.
     */
    void onUpdate(BaseAgent agent);
}
