package cobweb3d.core.location;

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
