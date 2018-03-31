package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.entity.Cause;

public interface EnergyMutator extends AgentMutator {

    void onEnergyChange(BaseAgent agent, int delta, Cause cause);
}
