package cobweb3d.plugins;

import cobweb3d.core.agent.BaseAgent;

public interface StateParameter {
    String getName();

    double getValue(BaseAgent agent);
}
