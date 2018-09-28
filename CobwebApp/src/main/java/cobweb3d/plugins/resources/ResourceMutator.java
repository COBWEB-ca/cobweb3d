package cobweb3d.plugins.resources;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.plugins.mutators.ConsumptionMutator;
import cobweb3d.plugins.states.AgentState;

public class ResourceMutator implements ConsumptionMutator {

    private ResourceParams params;

    private SimulationTimeSpace simulation;

    public ResourceMutator() {
    }

    @Override
    public void onConsumeAgent(BaseAgent agent, BaseAgent food) {
    }

    @Override
    public void onConsumeFood(BaseAgent agent, int foodType) {
        if (agent.getType() == foodType) {
            agent.changeEnergy(params.of(agent).energyGain);
        }
    }

    public <T extends AgentState> boolean acceptsState(Class<T> type, T value) {
        return false;
    }

}
