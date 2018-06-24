package cobweb3d.core.location;

import org.joml.Vector3i;

/**
 * This is a subclass of Vector3i, which is a well-written vector class in R^3.
 * This class represents the location of agents on a 3D grid.
 *
 * Document Author: Zewen Shen
 * Reminder: I'm not the author of this class, so my document may be inaccurate.
 */
public class Location extends Vector3i {
    public Location(int x, int y, int z) {
        super(x, y, z);
    }

    public Location() {
    }
}
