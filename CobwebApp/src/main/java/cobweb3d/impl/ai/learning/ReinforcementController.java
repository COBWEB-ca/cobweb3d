package cobweb3d.impl.ai.learning;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.agent.Controller;
import cobweb3d.core.agent.ControllerListener;
import cobwebutil.RandomNoGenerator;

public class ReinforcementController implements Controller {
    RandomNoGenerator random;

    public ReinforcementController() {
        random = new RandomNoGenerator();
    }

    @Override
    public void controlAgent(BaseAgent theAgent, ControllerListener inputCallback) {

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
