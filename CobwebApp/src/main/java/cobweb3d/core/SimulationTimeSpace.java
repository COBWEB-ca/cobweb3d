package cobweb3d.core;

import cobweb3d.core.environment.BaseEnvironment;
import cobweb3d.core.environment.Topology;

/**
 * This interface is a sub-interface of interface RandomSource, which is a random generator interface.
 * So generally any class implementing this interface must be able to return a time, the topology of the environment
 * and able to generate random number.
 */
public interface SimulationTimeSpace extends RandomSource {
    long getTime();

    Topology getTopology();

    BaseEnvironment getEnvironment();
}
