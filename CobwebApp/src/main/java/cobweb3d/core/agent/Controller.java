package cobweb3d.core.agent;


/**
 * The "brain" of an agent, the controller causes the controlled agent to act by calling methods on
 * it. The Controller is notified by a call to control agent that the agent is requesting guidance.
 */
public interface Controller {

    /**
     * Cause the specified agent to act.
     *
     * @param theAgent agent to control
     */
    void controlAgent(BaseAgent theAgent, ControllerListener inputCallback);

    /**
     * Creates controller for child based on parameters of the asexual breeding parent
     */
    Controller createChildAsexual();

    /**
     * Creates controller for child based on parameters of the sexual breeding parents
     *
     * @param parent2 second parent
     */
    Controller createChildSexual(Controller parent2);
}
