package cobweb3d.core;

import cobweb3d.core.environment.Topology;

public interface SimulationTimeSpace extends RandomSource {
    long getTime();

    Topology getTopology();
}
