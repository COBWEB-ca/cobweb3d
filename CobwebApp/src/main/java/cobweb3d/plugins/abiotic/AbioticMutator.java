package cobweb3d.plugins.abiotic;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.location.Location;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.StateParameter;
import cobweb3d.plugins.abiotic.factor.AbioticFactor;
import cobweb3d.plugins.abiotic.factor.AbioticFactorState;
import cobweb3d.plugins.abiotic.preference.AbioticPreferenceParam;
import cobweb3d.plugins.mutators.EnvironmentMutator;
import cobweb3d.plugins.mutators.SpawnMutator;
import cobweb3d.plugins.mutators.StatefulMutatorBase;
import cobweb3d.plugins.mutators.StepMutator;

import java.util.ArrayList;
import java.util.List;

public class AbioticMutator extends StatefulMutatorBase<AbioticState, AbioticParams> implements StepMutator, SpawnMutator, EnvironmentMutator {
    public AbioticParams params;
    private SimulationTimeSpace sim;

    public AbioticMutator() {
        super(AbioticState.class);
    }

    @Override
    protected boolean validState(AbioticState value) {
        return false;
    }


    /**
     * @param loc location.
     * @return The abiotic factor value at location
     */
    public float getValue(int factor, Location loc) {
        float x = (float) loc.x / sim.getTopology().width;
        float y = (float) loc.y / sim.getTopology().height;
        float z = (float) loc.z / sim.getTopology().depth;

        AbioticFactor abioticFactor = params.factors.get(factor);
        float value = abioticFactor.getValue(x, y, z);
        return value;
    }

    /**
     * @param l agent location.
     * @param state Agent type specific parameters.
     * @return The effect the abiotic factor has on the agent. 0 if agent is unaffected.
     */
    private float effectAtLocation(int factor, Location l, AbioticState state) {
        float temp = getValue(factor, l);
        AbioticPreferenceParam agentFactorParams = state.factorStates[factor].agentParams.preference;

        return agentFactorParams.score(temp);
    }

    @Override
    public boolean acceptsParam(Class<?> object) {
        return object.isAssignableFrom(AbioticParams.class);
    }

    @Override
    public void loadNew() {

    }

    @Override
    public void onStep(BaseAgent agent, Location from, Location to) {
        if (to == null) {
            return;
        }

        AbioticState state = getAgentState(agent);
        for (int i = 0; i < params.factors.size(); i++) {
            AbioticFactorState factorState = state.factorStates[i];
            float effect = effectAtLocation(i, to, state);
            float multiplier = 1 + effect;

            factorState.agentParams.parameter.modifyValue(causeKeys[i], agent, multiplier);
        }
    }

    @Override
    public void onSpawn(BaseAgent agent) {
        AbioticAgentParams aPar = params.agentParams[agent.getType()].clone();
        setAgentState(agent, new AbioticState(aPar));
    }

    @Override
    public void onSpawn(BaseAgent agent, BaseAgent parent) {
        setAgentState(agent, getAgentState(parent).clone());
    }

    @Override
    public void onSpawn(BaseAgent agent, BaseAgent parent1, BaseAgent parent2) {
        BaseAgent parent = sim.getRandom().nextBoolean() ? parent1 : parent2;
        onSpawn(agent, parent);
    }

    @Override
    public void onDeath(BaseAgent agent) {

    }

    /**
     * Sets the parameters according to the simulation configuration.
     * @param sim simulation sim
     * @param params abiotic parameters from the simulation configuration.
     */
    public void setParams(SimulationTimeSpace sim, AbioticParams params, int AgentType) {
        System.out.println('a');
        this.params = params;
        this.sim = sim;

        causeKeys = new CauseKey[params.factors.size()];
        for (int i = 0 ; i < causeKeys.length; i++) {
            causeKeys[i] = new CauseKey(i);
        }
    }

    private CauseKey[] causeKeys;

    private class CauseKey {
        private int index;
        public CauseKey(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return AbioticMutator.this.toString() + ".factor[" + index + "]";
        }
    }

    public List<StateParameter> getParameters() {
        List<StateParameter> res = new ArrayList<>(params.factors.size());
        for (int i = 0; i < params.factors.size(); i++) {
            res.add(new AbioticStatePenalty(i));
        }
        return res;
    }
    /**
     * Magnitude of abiotic discomfort the agent is experiencing
     */
    private class AbioticStatePenalty implements StateParameter {

        private int factor;

        public AbioticStatePenalty(int factor) {
            this.factor = factor;
        }

        @Override
        public String getName() {
            return String.format(AbioticParams.STATE_NAME_ABIOTIC_PENALTY, factor + 1);
        }

        @Override
        public double getValue(BaseAgent agent) {
            double value = Math.abs(effectAtLocation(factor, agent.getPosition(), getAgentState(agent)));
            return value;
        }

    }

    @Override
    public void update() {
        for (AbioticFactor f : params.factors) {
            f.update(sim);
        }
    }


}
