package cobweb3d.core.location;

import org.joml.Vector3i;

/**
 * Location on a 3D grid.
 */
public class Location extends Vector3i {
    public Location(int x, int y, int z) {
        super(x, y, z);
    }

    public Location() {
    }
}
