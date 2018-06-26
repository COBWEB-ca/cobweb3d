package cobweb3d.core;

import cobwebutil.RandomNoGenerator;

/**
 * Classes implementing this should be able to return a random number generator.
 */
public interface RandomSource {

    RandomNoGenerator getRandom();
}
