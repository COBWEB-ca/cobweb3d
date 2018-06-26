package cobweb3d.core.location;

import org.joml.Vector3i;

/**
 * This is a subclass of Vector3i that represents the position in R^3.
 */
public class Location extends Vector3i {
    public Location(int x, int y, int z) {
        super(x, y, z);
    }

    public Location() {
    }
}
