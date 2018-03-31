package cobweb3d.plugins.transform;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.Simulation;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.exchange.ExchangeState;
import cobweb3d.plugins.mutators.ConsumptionMutator;
import cobweb3d.plugins.mutators.StatefulMutatorBase;
import cobweb3d.plugins.mutators.UpdateMutator;

public class TransformationMutator extends StatefulMutatorBase<TransformationState, TransformationParams> implements UpdateMutator, ConsumptionMutator {
    TransformationParams params;
    private SimulationTimeSpace simulation;

    //private TransformationDataLogger transformationDataLogger;

    public TransformationMutator() {
        super(TransformationState.class);
    }

    @Override
    public void setParams(SimulationTimeSpace sim, TransformationParams transformationParams, int agentTypes) {
        this.simulation = sim;
        this.params = transformationParams;
        //this.transformationDataLogger = new TransformationDataLogger(agentTypes);
    }

    @Override
    public boolean acceptsParam(Class<?> object) {
        return object.isAssignableFrom(TransformationParams.class);
    }

    @Override
    protected boolean validState(TransformationState value) {
        return value != null;
    }

    @Override
    public void onUpdate(BaseAgent agent) {
        if (params.of(agent).exchangeTransformationAgentParams.enabled && getX(agent) >= params.of(agent).exchangeTransformationAgentParams.transformationX.getValue()) {
            setAgentState(agent, new TransformationState(getAgentState(agent), agent.getType(), (int) simulation.getTime()));
            agent.transformType(params.of(agent).exchangeTransformationAgentParams.destType - 1);
            if (agent instanceof Agent) {
                ((Agent) agent).setParams(((Simulation) simulation).environment.agentParams[agent.getType()]);
            }
        }
    }

    @Override
    public void onConsumeAgent(BaseAgent agent, BaseAgent food) {
        if (params.of(agent).consumptionTransformationAgentParams.canTransform[food.getType()]) {
            setAgentState(agent, new TransformationState(getAgentState(agent), agent.getType(), (int) simulation.getTime()));
            agent.transformType(params.of(agent).consumptionTransformationAgentParams.destType[food.getType()] - 1);
            if (agent instanceof Agent) {
                ((Agent) agent).setParams(((Simulation) simulation).environment.agentParams[agent.getType()]);
            }
        }
    }

    private float getX(BaseAgent agent) {
        if (agent instanceof Agent) {
            ExchangeState exchangeState = ((Agent) agent).getState(ExchangeState.class);
            if (exchangeState != null) {
                return exchangeState.x;
            }
        }
        return 0;
    }

    /*@Override
    public String getName() {
        return transformationDataLogger.getName();
    }

    @Override
    public void logData(BaseStatsProvider statsProvider) {
        transformationDataLogger.logData(statsProvider);
    }

    @Override
    public int getTableCount() {
        return transformationDataLogger.getTableCount();
    }

    @Override
    public Collection<DataTable> getTables() {
        return transformationDataLogger.getTables();
    }*/
}
