package cobweb3d.core;

import cobwebutil.RandomNoGenerator;

/**
 * Classes implementing this should be able to return a random number generator.
 *
 * Document Author: Zewen Shen
 * Reminder: I'm not the author of this class, so my documentation may be inaccurate.
 */
public interface RandomSource {

    RandomNoGenerator getRandom();
}
