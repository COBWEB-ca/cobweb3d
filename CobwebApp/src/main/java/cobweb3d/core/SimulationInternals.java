package cobweb3d.core;

import cobweb3d.core.agent.AgentListener;
import cobweb3d.core.agent.BaseAgent;


/**
 * Methods that only simulation components need access to.
 * UI and other external components should only use SimulationInterface!
 */
public interface SimulationInternals extends SimulationTimeSpace {
    BaseAgent newAgent(int type);

    void registerAgent(BaseAgent agent);

    AgentListener getAgentListener();
}
