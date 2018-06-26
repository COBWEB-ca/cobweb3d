package cobweb3d.core.location;

/**
 * This class combines Location and Direction together, such that it can represent
 * both one agent's moving direction and current position.
 * For other references, please read class Direction and Location.
 */
public class LocationDirection extends Location {
    public Direction direction;

    public LocationDirection(Location l, Direction d) {
        super(l.x, l.y, l.z);
        direction = d;
    }

    public LocationDirection(Location l) {
        this(l, new Direction(0, 0, 0));
    }

    @Override
    public String toString() {
        return "LocationDirection{" +
                "direction=" + direction +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
