package cobweb3d.impl.ai.random;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.agent.Controller;
import cobweb3d.core.agent.ControllerListener;
import cobweb3d.impl.agent.Agent;
import cobwebutil.RandomNoGenerator;

/**
 * This class determines the survival strategy of one agent.
 */
public class SimpleController implements Controller {

    RandomNoGenerator random;

    public SimpleController() {
        random = new RandomNoGenerator();
    }

    /**
     * This is a simple demo survival strategy, which randomly turn around or move forward.
     *
     * @param agent the agent who is controlled by this strategy
     * @param inputCallback
     */
    @Override
    public void controlAgent(BaseAgent agent, ControllerListener inputCallback) {
        if (agent instanceof Agent) {
            Agent theAgent = (Agent) agent;
            int action = random.nextIntRange(1, 10);

            switch (action) {
                case 1: //xPos
                    theAgent.turnLeft();
                    break;
                case 2:
                    theAgent.turnRight();
                    break;
                case 3:
                    theAgent.turnUp();
                    break;
                case 4:
                    theAgent.turnDown();
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    theAgent.step();
                    break;
            }
        }
    }

    @Override
    public Controller createChildAsexual() {
        return new SimpleController();
    }

    @Override
    public Controller createChildSexual(Controller parent2) {
        return new SimpleController();
    }
}
