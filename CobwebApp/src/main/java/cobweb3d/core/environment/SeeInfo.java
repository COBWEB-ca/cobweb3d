package cobweb3d.core.environment;


import cobweb3d.core.location.Location;

public class SeeInfo {
    private final int distance;

    private final int type;

    private final Location location;


    private static final long serialVersionUID = 1L;

    /**
     * Contains the information of what the agent sees.
     *  @param distance Distance to t.
     * @param type Type of object seen.
     * @param location the location of that object.
     */
    public SeeInfo(int distance, int type, Location location) {
        this.distance = distance;
        this.type = type;
        this.location = location;
    }

    /**
     * @return How far away the object is.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * @return What the agent sees (rock, food, etc.)
     */
    public int getType() {
        return type;
    }

}
