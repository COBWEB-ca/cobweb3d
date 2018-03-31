package cobweb3d.impl;

import cobweb3d.core.params.AgentFoodCountable;

import java.util.List;


public interface SimulationParams extends AgentFoodCountable {
    List<String> getPluginParameters();
}
