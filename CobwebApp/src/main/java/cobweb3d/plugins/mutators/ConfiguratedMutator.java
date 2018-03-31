package cobweb3d.plugins.mutators;

import cobweb3d.core.SimulationTimeSpace;

public interface ConfiguratedMutator<T> {
    void setParams(SimulationTimeSpace sim, T params, int agentTypes);

    boolean acceptsParam(Class<?> type);
}
