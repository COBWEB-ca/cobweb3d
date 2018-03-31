package cobweb3d.plugins.diminish;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.mutators.ConfiguratedMutator;
import cobweb3d.plugins.mutators.ContactMutator;
import cobweb3d.plugins.states.AgentState;

public class DiminishMutator implements ContactMutator, ConfiguratedMutator<DiminishParams> {

    DiminishParams params;

    private SimulationTimeSpace simulation;

    public DiminishMutator() {
    }

    @Override
    public void setParams(SimulationTimeSpace sim, DiminishParams reproductionParams, int agentTypes) {
        this.simulation = sim;
        this.params = reproductionParams;
    }

    @Override
    public boolean acceptsParam(Class<?> object) {
        return object.isAssignableFrom(DiminishParams.class);
    }

    @Override
    public <T extends AgentState> boolean acceptsState(Class<T> type, T value) {
        return false;
    }

    private boolean canDiminish(BaseAgent attacker, BaseAgent victim) {
        return attacker != null && victim != null && params.of(attacker).canDiminish[victim.getType()];
    }

    private void diminish(BaseAgent attacker, BaseAgent victim) {
        victim.changeEnergy(-params.of(attacker).energyDiminishedAbs[victim.getType()], new DiminishAgentCause());
    }

    @Override
    public void onContact(BaseAgent bumper, BaseAgent bumpee) {
        if (canDiminish(bumper, bumpee)) diminish(bumper, bumpee);
    }

    public static class DiminishAgentCause extends Agent.EatCause {
        @Override
        public String getName() {
            return "Diminish Agent";
        }
    }
}
