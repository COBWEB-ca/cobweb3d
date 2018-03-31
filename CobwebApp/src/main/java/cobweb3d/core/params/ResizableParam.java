package cobweb3d.core.params;

import cobwebutil.io.ParameterSerializable;

public interface ResizableParam extends ParameterSerializable {

    /**
     * Updates configuration for the new number of agent types
     *
     * @param envParams used to retrieve agent type count
     */
    void resize(AgentFoodCountable envParams);
}
