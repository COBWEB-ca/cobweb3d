package cobweb3d.plugins.abiotic;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.location.Location;
import cobweb3d.plugins.abiotic.factor.AbioticFactor;
import cobweb3d.plugins.mutators.EnvironmentMutator;
import cobweb3d.plugins.mutators.SpawnMutator;
import cobweb3d.plugins.mutators.StatefulMutatorBase;
import cobweb3d.plugins.mutators.StepMutator;

public class AbioticMutator extends StatefulMutatorBase<AbioticState, AbioticParams> implements StepMutator, SpawnMutator, EnvironmentMutator {
    public AbioticParams params;
    private SimulationTimeSpace sim;

    public AbioticMutator(Class<AbioticState> stateClass) {
        super(AbioticState.class);
    }

    @Override
    protected boolean validState(AbioticState value) {
        return false;
    }

    @Override
    public void setParams(SimulationTimeSpace sim, AbioticParams params, int agentTypes) {

    }
    /**
     * @param loc location.
     * @return The abiotic factor value at location
     */
    public float getValue(int factor, Location loc) {
        float x = (float) loc.x / sim.getTopology().width;
        float y = (float) loc.y / sim.getTopology().height;

        AbioticFactor abioticFactor = params.factors.get(factor);
        float value = abioticFactor.getValue(x, y);
        return value;
    }

    @Override
    public boolean acceptsParam(Class<?> type) {
        return false;
    }

    @Override
    public void loadNew() {

    }

    @Override
    public void onStep(BaseAgent agent, Location from, Location to) {

    }

    @Override
    public void onDeath(BaseAgent agent) {

    }

    @Override
    public void onSpawn(BaseAgent agent) {

    }
}
