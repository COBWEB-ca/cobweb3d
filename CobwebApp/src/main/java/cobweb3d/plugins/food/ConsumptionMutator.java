package cobweb3d.plugins.food;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.mutators.ConfiguratedMutator;
import cobweb3d.plugins.mutators.ContactMutator;
import cobweb3d.plugins.states.AgentState;

public class ConsumptionMutator implements ContactMutator, ConfiguratedMutator<ConsumptionParams> {

    ConsumptionParams params;

    private SimulationTimeSpace simulation;

    public ConsumptionMutator() {
    }

    @Override
    public void setParams(SimulationTimeSpace sim, ConsumptionParams reproductionParams, int agentTypes) {
        this.simulation = sim;
        this.params = reproductionParams;
    }

    @Override
    public boolean acceptsParam(Class<?> object) {
        return object.isAssignableFrom(ConsumptionParams.class);
    }

    @Override
    public <T extends AgentState> boolean acceptsState(Class<T> type, T value) {
        return false;
    }

    private boolean canEat(BaseAgent eater, BaseAgent food) {
        if (eater == null || food == null) return false;
        else return params.of(eater).canEat[food.getType()];
    }

    private void eat(BaseAgent eater, BaseAgent food) {
        int gain = food.getEnergy(); // TODO: later multiple by factor ?)
        eater.changeEnergy((int) (gain * params.of(eater).energyMultipler[food.getType()]), new Agent.EatAgentCause());
        food.die();
        // TODO: figure out a way for plugins to broadcast events for other plugins to listen to.
        if (simulation instanceof SimulationInternals)
            ((SimulationInternals) simulation).getAgentListener().onConsumeAgent(eater, food);
    }

    @Override
    public void onContact(BaseAgent bumper, BaseAgent bumpee) {
        if (canEat(bumper, bumpee)) eat(bumper, bumpee);
    }
}
