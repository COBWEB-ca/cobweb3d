package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;

public interface UpdateMutator extends AgentMutator {

    void onUpdate(BaseAgent agent);
}
