package cobweb3d.impl.ai.learning;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.agent.Controller;
import cobweb3d.core.agent.ControllerListener;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.vision.VisionState;
import cobwebutil.RandomNoGenerator;

import java.util.HashMap;
import java.util.Map;

public class ReinforcementController implements Controller {
    RandomNoGenerator random;
    Map<ReinforcementLearningState, Integer> decisionTable = new HashMap<>();

    public ReinforcementController() {
        random = new RandomNoGenerator();
    }

    @Override
    public void controlAgent(BaseAgent theAgent, ControllerListener inputCallback) {
        if (! (theAgent instanceof Agent)) return;
        VisionState vision = ((Agent) theAgent).getState(VisionState.class);
        if (vision == null) return;

    }

    @Override
    public Controller createChildAsexual() {
        return null;
    }

    @Override
    public Controller createChildSexual(Controller parent2) {
        return null;
    }
}
