package cobweb3d.core.params;

import java.util.List;


public interface SimulationParamConfig extends AgentFoodCountable {
    List<String> getPluginParameters();
}
