package cobweb3d.core.environment;


import cobweb3d.core.location.Location;

public class SeeInfo {
    private final int distance;

    private final ObjectType type;

    private final int index;

    private final Location location;


    private static final long serialVersionUID = 1L;

    /**
     * Contains the information of what the agent sees.
     *  @param distance Distance to t.
     * @param type Type of object seen.
     * @param location the location of that object.
     * @param index The index of object in the given type, E.g., if index = 1 and type = agent, then it represents agent 1.
     */
    public SeeInfo(ObjectType type, int index, Location location, int distance) {
        this.distance = distance;
        this.type = type;
        this.location = location;
        this.index = index;
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
    public ObjectType getType() {
        return type;
    }

}
