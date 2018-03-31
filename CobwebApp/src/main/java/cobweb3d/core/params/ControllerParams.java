package cobweb3d.core.params;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.agent.Controller;

/**
 * Configuration for a Controller
 */
public interface ControllerParams extends ResizableParam {

    /**
     * Creates Controller for given agent type
     *
     * @param sim  simulation the agent is in
     * @param type agent type
     * @return Controller for agent
     */
    Controller createController(SimulationInternals sim, int type);
}
