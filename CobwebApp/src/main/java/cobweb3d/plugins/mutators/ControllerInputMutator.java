package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.agent.ControllerInput;

public interface ControllerInputMutator extends AgentMutator {

    /**
     * Controller has controlled an agent.
     *
     * @param agent  BaseAgent in question.
     * @param cInput controller input state
     */
    void onControl(BaseAgent agent, ControllerInput cInput);
}
