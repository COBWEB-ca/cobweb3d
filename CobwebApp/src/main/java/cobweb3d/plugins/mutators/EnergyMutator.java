package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.entity.Cause;

public interface EnergyMutator extends AgentMutator {

    /**
     * Energy of one agent is changed.
     *
     * @param agent agent whose energy changed.
     * @param delta the amount of changing energy.
     * @param cause the reason why it changes.
     */
    void onEnergyChange(BaseAgent agent, int delta, Cause cause);
}
