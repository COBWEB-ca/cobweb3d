package cobweb3d.core.location;

import org.joml.Vector3i;


public class Direction extends Vector3i {
    public static Direction xPos = new Direction(1, 0, 0);
    public static Direction xNeg = new Direction(-1, 0, 0);
    public static Direction yPos = new Direction(0, 1, 0);
    public static Direction yNeg = new Direction(0, -1, 0);
    public static Direction zPos = new Direction(0, 0, 1);
    public static Direction zNeg = new Direction(0, 0, -1);
    public static Direction NONE = new Direction(0, 0, 0);

    public static Direction[] XYZDirs = {xNeg, xPos, yNeg, yPos, zNeg, zPos};
    public static Direction[] XZDirs = {xNeg, xPos, zNeg, zPos};

    public Direction(int x, int y, int z) {
        super(x, y, z);
    }

    public Direction setD(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Direction setD(Direction d) {
        this.x = d.x;
        this.y = d.y;
        this.z = d.z;
        return this;
    }
}
