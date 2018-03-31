package cobweb3d.plugins.states;

import cobwebutil.io.ParameterSerializable;

public interface AgentState extends ParameterSerializable {

    boolean isTransient();
}
