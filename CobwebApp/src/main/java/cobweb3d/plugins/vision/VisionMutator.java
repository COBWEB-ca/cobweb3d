package cobweb3d.plugins.vision;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.environment.BaseEnvironment;
import cobweb3d.core.environment.Topology;
import cobweb3d.core.location.Location;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.plugins.mutators.StatefulMutatorBase;
import cobweb3d.plugins.mutators.StepMutator;
import cobweb3d.plugins.states.AgentState;

import java.util.List;

public class VisionMutator extends StatefulMutatorBase<VisionState, VisionParams> implements StepMutator {
    VisionParams params;
    private SimulationTimeSpace simulation;

    public VisionMutator() {
        super(VisionState.class);
    }


    @Override
    public boolean acceptsParam(Class<?> object) {
        return object.isAssignableFrom(VisionParams.class);
    }

    @Override
    protected boolean validState(VisionState value) {
        return value != null;
    }


    @Override
    public void setParams(SimulationTimeSpace sim, VisionParams params, int agentTypes) {
        this.params = params;
        this.simulation = sim;
    }



    @Override
    public void onStep(BaseAgent agent, Location from, Location to) {
        Topology topology = simulation.getTopology();
        VisionAgentParams agentParams = params.of(agent);
        List<Location> seeableArea =
                topology.getSeeableArea((LocationDirection)to, agentParams.frontEyesight, agentParams.backEyesight,
               agentParams.leftEyesight, agentParams.rightEyesight, agentParams.upEyesight, agentParams.downEyesight);
    }


}
