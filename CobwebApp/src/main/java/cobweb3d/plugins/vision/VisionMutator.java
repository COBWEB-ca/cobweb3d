package cobweb3d.plugins.vision;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.environment.BaseEnvironment;
import cobweb3d.core.environment.Topology;
import cobweb3d.core.location.Location;
import cobweb3d.plugins.mutators.StatefulMutatorBase;
import cobweb3d.plugins.mutators.StepMutator;
import cobweb3d.plugins.states.AgentState;

public class VisionMutator extends StatefulMutatorBase<VisionState, VisionParams> implements StepMutator {

    VisionParams params;
    private BaseEnvironment environment;

    public VisionMutator(Class<VisionState> stateClass) {
        super(VisionState.class);
    }


    @Override
    public boolean acceptsParam(Class type) {
        return false;
    }

    @Override
    protected boolean validState(VisionState value) {
        return false;
    }

    @Override
    public void setParams(SimulationTimeSpace sim, VisionParams params, int agentTypes) {

    }


    @Override
    public void onStep(BaseAgent agent, Location from, Location to) {
        Topology topology = environment.topology;
    }
}
