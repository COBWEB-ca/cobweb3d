package cobweb3d.core.environment;

import cobweb3d.core.RandomSource;
import cobweb3d.core.location.Direction;
import cobweb3d.core.location.Location;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.core.location.Rotation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * This class is like a manager class of class Direction, Location and LocationDirection.
 * It encapsulates all kinds of methods which takes Direction, Location and LocationDirection as inputs.
 */
public class Topology {
    public final int width;
    public final int height;
    public final int depth;
    private final boolean wrap; // wrap means whether or not one agent can move beyond the boundary of the space
                                // and appears again in the the opposite boundary.
    private RandomSource randomSource;

    public Topology(RandomSource randomSource, int width, int height, int depth, boolean wrap) {
        this.randomSource = randomSource;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.wrap = wrap;
    }

    public List<Location> getSeeableArea(LocationDirection location, int frontEyesight, int backEyesight, int leftEyesight,
                                        int rightEyesight, int upEyesight, int downEyesight) {
        List<Location> seeableArea = new ArrayList<>();
        int upperBound, lowerBound, leftBound, rightBound, frontBound, backBound;
        Location topLeftFront, topLeftBack, topRightFront, topRightBack, bottomLeftFront, bottomLeftBack, bottomRightFront, bottomRightBack;
        Direction direction = location.direction;
        if (direction.y == 0) { // faces to x or z
            upperBound = location.y - upEyesight;
            lowerBound = location.y + downEyesight;
            if (direction.z == 0) { // agent faces to x axis
                if (direction.x == 1) { // faces to x positive
                    leftBound = location.z + leftEyesight;
                    rightBound = location.z - rightEyesight;
                    frontBound = location.x + frontEyesight;
                    backBound = location.x - frontEyesight;
                    topLeftFront = new Location(frontBound, upperBound, leftBound);
                    topLeftBack = new Location(backBound, upperBound, leftBound);
                    topRightFront = new Location(frontBound, upperBound,rightBound);
                    topRightBack = new Location(backBound, upperBound, rightBound);
                    bottomLeftFront = new Location(frontBound, lowerBound, leftBound);
                    bottomLeftBack = new Location(backBound, lowerBound, leftBound);
                    bottomRightFront = new Location(frontBound, lowerBound, rightBound);
                    bottomRightBack = new Location(backBound, lowerBound, rightBound);
                } else { // faces to x negative
                    leftBound = location.z - leftEyesight;
                    rightBound = location.z + rightEyesight;
                    frontBound = location.x - frontEyesight;
                    backBound = location.x + frontEyesight;
                    topLeftFront = new Location(frontBound, upperBound, leftBound);
                    topLeftBack = new Location(backBound, upperBound, leftBound);
                    topRightFront = new Location(frontBound, upperBound,rightBound);
                    topRightBack = new Location(backBound, upperBound, rightBound);
                    bottomLeftFront = new Location(frontBound, lowerBound, leftBound);
                    bottomLeftBack = new Location(backBound, lowerBound, leftBound);
                    bottomRightFront = new Location(frontBound, lowerBound, rightBound);
                    bottomRightBack = new Location(backBound, lowerBound, rightBound);
                }
            } else {
                assert (direction.z == 1 || direction.z == -1); // faces to z axis
                if (direction.z == 1) { // faces to z positive
                    leftBound = location.x - leftEyesight;
                    rightBound = location.x + rightEyesight;
                    frontBound = location.z + frontEyesight;
                    backBound = location.z - backEyesight;
                    topLeftFront = new Location(leftBound, upperBound, frontBound);
                    topLeftBack = new Location(leftBound, upperBound, backBound);
                    topRightFront = new Location(rightBound, upperBound, frontBound);
                    topRightBack = new Location(rightBound, upperBound, backBound);
                    bottomLeftFront = new Location(leftBound, lowerBound, frontBound);
                    bottomLeftBack = new Location(leftBound, lowerBound, backBound);
                    bottomRightFront = new Location(rightBound, lowerBound, frontBound);
                    bottomRightBack = new Location(rightBound, lowerBound, backBound);
                } else { // faces to z negative
                    leftBound = location.x + leftEyesight;
                    rightBound = location.x - rightEyesight;
                    frontBound = location.z - frontEyesight;
                    backBound = location.z + backEyesight;
                    topLeftFront = new Location(leftBound, upperBound, frontBound);
                    topLeftBack = new Location(leftBound, upperBound, backBound);
                    topRightFront = new Location(rightBound, upperBound, frontBound);
                    topRightBack = new Location(rightBound, upperBound, backBound);
                    bottomLeftFront = new Location(leftBound, lowerBound, frontBound);
                    bottomLeftBack = new Location(leftBound, lowerBound, backBound);
                    bottomRightFront = new Location(rightBound, lowerBound, frontBound);
                    bottomRightBack = new Location(rightBound, lowerBound, backBound);
                }
            }
            for (int i = min(topLeftFront.x, topLeftBack.x); i <= max(topLeftFront.x, topLeftBack.x); i++) {
                for (int j = min(topLeftFront.y, bottomLeftFront.y); j <= max(topLeftFront.y, bottomLeftFront.y); j++) {
                    for (int k = min(topLeftFront.z, topRightFront.z); k <= max(topLeftFront.z, topRightFront.z); k++) {
                        seeableArea.add(new Location(i, j, k));
                    }
                }
            }
        } else {
            if (direction.y == -1) { // facing y negative
                upperBound = location.z - upEyesight;
                lowerBound = location.z + downEyesight;
                leftBound = location.x - leftEyesight;
                rightBound = location.x + rightEyesight;
                frontBound = location.y - frontEyesight;
                backBound = location.y + backEyesight;
                topLeftFront = new Location(leftBound, frontBound, upperBound);
                topLeftBack = new Location(leftBound, backBound, upperBound);
                topRightFront = new Location(rightBound, frontBound, upperBound);
                topRightBack = new Location(rightBound, backBound, upperBound);
                bottomLeftFront = new Location(leftBound, frontBound, lowerBound);
                bottomLeftBack = new Location(leftBound, backBound, lowerBound);
                bottomRightFront = new Location(rightBound, frontBound, lowerBound);
                bottomRightBack = new Location(rightBound, backBound, lowerBound);
            } else { // facing y positive
                assert (direction.y == 1);
                upperBound = location.z + upEyesight;
                lowerBound = location.z - downEyesight;
                leftBound = location.x - leftEyesight;
                rightBound = location.x + rightEyesight;
                frontBound = location.y + frontEyesight;
                backBound = location.y - backEyesight;
                topLeftFront = new Location(leftBound, frontBound, upperBound);
                topLeftBack = new Location(leftBound, backBound, upperBound);
                topRightFront = new Location(rightBound, frontBound, upperBound);
                topRightBack = new Location(rightBound, backBound, upperBound);
                bottomLeftFront = new Location(leftBound, frontBound, lowerBound);
                bottomLeftBack = new Location(leftBound, backBound, lowerBound);
                bottomRightFront = new Location(rightBound, frontBound, lowerBound);
                bottomRightBack = new Location(rightBound, backBound, lowerBound);
            }
            for (int i = min(topLeftFront.x, topRightFront.x); i <= max(topLeftFront.x, topRightFront.x); i++) {
                for (int j = min(topLeftFront.y, topLeftBack.y); j <= max(topLeftFront.y, topLeftBack.y); j++) {
                    for (int k = min(topLeftFront.z, bottomLeftFront.z); k <= max(topLeftFront.z, bottomLeftFront.z); k++) {
                        seeableArea.add(new Location(i, j, k));
                    }
                }
            }
        }
        return seeableArea;
    }
    public Location getAdjacent(Location location, Direction direction) {
        return getAdjacent(new LocationDirection(location, direction));
    }

    public Location getRandomLocation() {
        Location l;
        do {
            l = new Location(
                    randomSource.getRandom().nextInt(width),
                    randomSource.getRandom().nextInt(height),
                    randomSource.getRandom().nextInt(depth));
        } while (!isValidLocation(l));
        return l;
    }

    public boolean isValidLocation(int x, int y, int z) {
        return x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth;
    }
    public boolean isValidLocation(Location l) {
        return l != null
                && l.x >= 0 && l.x < width
                && l.y >= 0 && l.y < height
                && l.z >= 0 && l.z < depth;
    }

    /**
     * @param location The position and direction of the current agent
     * @return the LocationDirection which is in front of the agent
     */
    public LocationDirection getAdjacent(LocationDirection location) {
        Direction direction = location.direction;
        int x = location.x + direction.x;
        int y = location.y + direction.y;
        int z = location.z + direction.z;

        if (wrap) {
            x = (x + width) % width;
            z = (z + depth) % depth;
            boolean flip = false;
            if (y < 0) {
                y = -y - 1;
                flip = true;
            } else if (y >= height) {
                y = height * 2 - y - 1;
                flip = true;
            }
            if (flip) {
                x = (x + width / 2) % width;
                z = (z + depth / 2) % depth; // TODO ????
                direction = new Direction(-direction.x, -direction.y, -direction.z);
            }
        } else {
            if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth)
                return location;
        }
        return new LocationDirection(new Location(x, y, z), direction);
    }

    private List<Location> getWrapVirtualLocations(Location l) {
        List<Location> result = new ArrayList<Location>(7);
        result.add(l);

     /*   if (wrap) {
            // wrap left
            result.add(new Location(l.x - width, l.y));
            // wrap right
            result.add(new Location(l.x + width, l.y));

            // wrap down left
            result.add(new Location(l.x - width + width / 2, 2 * height - l.y - 1));
            // wrap down right
            result.add(new Location(l.x + width / 2,         2 * height - l.y - 1));

            // wrap up left
            result.add(new Location(l.x - width + width / 2, - l.y - 1));
            // wrap up right
            result.add(new Location(l.x + width / 2,         - l.y - 1));
        } */

        return result;
    }

    private Location getClosestWrapLocation(Location zero, Location target) {
        if (!wrap)
            return target;

        double distance = Double.MAX_VALUE;
        Location best = target;
        for (Location virtual : getWrapVirtualLocations(target)) {
            double d = zero.distanceSquared(virtual);
            if (d < distance) {
                distance = d;
                if (best != virtual)
                    best = virtual;
            }
        }
        return best;
    }

    public Set<Location> getArea(Location zero, float radius) {
        Set<Location> result = new HashSet<Location>();

        if (!wrap) {
            int radInt = (int) Math.ceil(radius);
            int x0 = min(zero.x - radInt, 0);
            int x1 = max(zero.x + radInt, width - 1);
            int y0 = min(zero.y - radInt, 0);
            int y1 = max(zero.y + radInt, height - 1);
            int z0 = min(zero.z - radInt, 0);
            int z1 = max(zero.z + radInt, height - 1);

            for (int x = x0; x < x1; x++) {
                for (int y = y0; y < y1; y++) {
                    for (int z = z0; z < z1; z++) {
                        Location l = new Location(x, y, z);
                        if (zero.distance(l) <= radius) {
                            result.add(l);
                        }
                    }
                }
            }
        } else {
            int maxRadius = max(max(width, height), depth);
            if (radius > maxRadius) radius = maxRadius;

            result.add(zero);
            LocationDirection l = new LocationDirection(zero, Direction.zPos);

            // walk around the zero point in increasingly larger rectangles
           /* for (int r = 1; r <= radius; r++) {
                // North-center point
                l = getAdjacent(l);
                result.add(l);

                // length of walks along each side of the rectangle
                // North-center to NE to SE to SW to NW, back to North-center
                int[] sides = {r, r*2, r*2, r*2, r-1};

                for (int side : sides) {
                    l = getTurnRightPosition(l);
                    for (int i = 0; i < side; i++) {
                        l = getAdjacent(l);
                        if (zero.distance(l) <= radius)
                            result.add(l);
                    }
                }

                // finish back at top-center, facing up
                l = getAdjacent(l);
                l = getTurnLeftPosition(l);
            }*/
            // TODO: Wrapping!
        }

        return result;
    }

    /*public LocationDirection getTurnRightPosition(LocationDirection location) {
        if (location == null || location.direction == null) return location;
        return new LocationDirection(location, turnRight(location.direction));
    }

    public LocationDirection getTurnLeftPosition(LocationDirection location) {
        if (location == null || location.direction == null) return location;
        return new LocationDirection(location, turnLeft(location.direction));
    }

    public LocationDirection getTurnUpPosition(LocationDirection location) {
        if (location == null || location.direction == null) return location;
        return new LocationDirection(location, turnUp(location.direction));
    }

    public LocationDirection getTurnDownPosition(LocationDirection location) {
        if (location == null || location.direction == null) return location;
        return new LocationDirection(location, turnDown(location.direction));
    }

    /*protected Direction turnRight(Direction dir) {
        if (dir.y != 0) {
            return new Direction(-dir.y, -dir.x, 0);
        } else {
            if (dir.x == 0 && dir.z == 0) return getRandomXZDir();
            else return new Direction(-dir.z, 0, dir.x);
        }
    }

    protected Direction turnLeft(Direction dir) {
        if (dir.y != 0) {
            return new Direction(dir.y, dir.x, 0);
        } else {
            if (dir.x == 0 && dir.z == 0) return getRandomXZDir();
            else return new Direction(-dir.z, 0, dir.x);
        }
    }

    protected Direction turnUp(Direction dir) {
        if (dir.y != 0) {
            return new Direction(0, -dir.z, dir.y);
        } else {
            if (dir.x == 0) return new Direction(0, -dir.z, 0);
            else if (dir.z == 0) return new Direction(0, -dir.x, 0);
            else return Direction.yNeg;
        }
    }

    protected Direction turnDown(Direction dir) {
        if (dir.y != 0) {
            return new Direction(0, dir.z, -dir.y);
        } else {
            if (dir.x == 0) return new Direction(0, dir.z, 0);
            else if (dir.z == 0) return new Direction(0, dir.x, 0);
            else return Direction.yPos;
        }
    }*/
    public LocationDirection getTurnRightPosition(LocationDirection location) {
        if (location == null || location.direction == null) return location;
        turnRight(location.direction);
        return location;
    }

    public LocationDirection getTurnLeftPosition(LocationDirection location) {
        if (location == null || location.direction == null) return location;
        turnLeft(location.direction);
        return location;
    }

    public LocationDirection getTurnUpPosition(LocationDirection location) {
        if (location == null || location.direction == null) return location;
        turnUp(location.direction);
        return location;
    }

    public LocationDirection getTurnDownPosition(LocationDirection location) {
        if (location == null || location.direction == null) return location;
        turnDown(location.direction);
        return location;
    }

    protected Direction turnRight(Direction dir) {
        if (dir.y != 0) {
            return dir.setD(-dir.y, -dir.x, 0);
        } else {
            if (dir.x == 0 && dir.z == 0) return dir.setD(getRandomXZDir());
            else return dir.setD(-dir.z, 0, dir.x);
        }
    }

    protected Direction turnLeft(Direction dir) {
        if (dir.y != 0) {
            return dir.setD(dir.y, dir.x, 0);
        } else {
            if (dir.x == 0 && dir.z == 0) return dir.setD(getRandomXZDir());
            else return dir.setD(-dir.z, 0, dir.x);
        }
    }

    protected Direction turnUp(Direction dir) {
        if (dir.y != 0) {
            return dir.setD(0, -dir.z, dir.y);
        } else {
            if (dir.x == 0) return dir.setD(0, -dir.z, 0);
            else if (dir.z == 0) return dir.setD(0, -dir.x, 0);
            else return dir.setD(Direction.yNeg);
        }
    }

    protected Direction turnDown(Direction dir) {
        if (dir.y != 0) {
            return dir.setD(0, dir.z, -dir.y);
        } else {
            if (dir.x == 0) return dir.setD(0, dir.z, 0);
            else if (dir.z == 0) return dir.setD(0, dir.x, 0);
            else return dir.setD(Direction.yPos);
        }
    }

    public Rotation getRotationBetween(Direction from, Direction to) {
        if (from.equals(to))
            return Rotation.None;
        else if (turnRight(from).equals(to))
            return Rotation.Right;
        else if (turnLeft(from).equals(to))
            return Rotation.Left;
        else
            return Rotation.UTurn;
    }

    public Direction getDirectionBetween6way(Location from, Location to) {
        to = getClosestWrapLocation(from, to);

        int d[] = {to.x - from.x, to.y - from.y, to.z - from.z};
        if (d[0] == 0 && d[1] == 0 && d[2] == 0)
            return Direction.NONE;

        // Since we are limited to axis directions, we can take the maximum delta as an approximation.
        int maxIndex = 0;
        int absMax = Integer.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
            int absVal = Math.abs(d[i]);
            if (absVal > absMax) {
                maxIndex = i;
                absMax = absVal;
            }
        }

        switch (maxIndex) {
            case 0:
                if (d[maxIndex] <= 0) return Direction.xNeg;
                else return Direction.xPos;
            case 1:
                if (d[maxIndex] <= 0) return Direction.yNeg;
                else return Direction.yPos;
            case 2:
                if (d[maxIndex] <= 0) return Direction.zNeg;
                else return Direction.zPos;
        }

        return Direction.NONE;
    }

    public Direction getDirectionBetween0way(Location from, Location to) {
        to = getClosestWrapLocation(from, to);

        int d[] = {to.x - from.x, to.y - from.y, to.z - from.z};
        if (d[0] == 0 && d[1] == 0 && d[2] == 0)
            return Direction.NONE;

        // Since we are limited to axis directions, we can take the maximum delta as an approximation.
        int minIndex = 0;
        int absMin = Integer.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            int absVal = Math.abs(d[i]);
            if (absVal < absMin) {
                minIndex = i;
                absMin = absVal;
            }
        }

        switch (minIndex) {
            case 0: // X is min
                return new Direction(0, round(d[1]), round(d[2])); // X - ZERO, YZ = +/- 1
            case 1:
                return new Direction(round(d[0]), 0, round(d[2]));
            case 2:
                return new Direction(round(d[0]), round(d[1]), 0);
        }

        return Direction.NONE;
    }

    private int round(int i) {
        if (i < 0) return -1;
        else if (i > 0) return 1;
        else return 0;
    }

    // Some predefined directions for 2D
    public Direction getRandomDirection() {
        return Direction.XYZDirs[randomSource.getRandom().nextInt(Direction.XYZDirs.length)];
    }

    public Direction getRandomXZDir() {
        return Direction.XZDirs[randomSource.getRandom().nextInt(Direction.XZDirs.length)];
    }
}
