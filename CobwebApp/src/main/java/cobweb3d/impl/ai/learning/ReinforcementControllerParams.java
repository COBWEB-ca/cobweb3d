package cobweb3d.impl.ai.learning;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.agent.Controller;
import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.ControllerParams;
import cobweb3d.core.params.PerAgentParams;
import cobweb3d.impl.SimulationParams;
import cobwebutil.io.ParameterSerializable;

import java.util.List;

public class ReinforcementControllerParams extends PerAgentParams<ReinforcementControllerParams.StateParams> implements ControllerParams {

    private static final long serialVersionUID = -6295295048720208508L;

    private final transient SimulationParams simParam;

    public ReinforcementControllerParams(SimulationParams simParam) {
        super(ReinforcementControllerParams.StateParams.class);
        this.simParam = simParam;
        resize(simParam);
    }

    @Override
    protected ReinforcementControllerParams.StateParams newAgentParam() {
        return new ReinforcementControllerParams.StateParams(simParam);
    }

    @Override
    public Controller createController(SimulationInternals sim, int type) {
        return new ReinforcementController();
    }

    @Override
    public void resize(AgentFoodCountable envParams) {
        super.resize(envParams);
        for (ReinforcementControllerParams.StateParams i : agentParams) {
            i.resize(simParam);
        }
    }

    public static class StateParams implements ParameterSerializable {
        private static final long serialVersionUID = 2L;

        @Deprecated // For reflection use only.
        public StateParams() {
        }

        public StateParams(SimulationParams simParam) {
            resize(simParam);
        }

        public void resize(SimulationParams simParam) {
            List<String> validParams = simParam.getPluginParameters();
        }
    }
}
