package cobweb3d.plugins.broadcast;


import cobweb3d.core.location.Location;
import cobweb3d.impl.agent.Agent;

public class FoodBroadcast extends BroadcastPacket {

    public final Location foodLocation;

    public FoodBroadcast(Location foodLocation, Agent dispatcherId) {
        super(dispatcherId);
        this.foodLocation = foodLocation;
    }

    @Override
    public void process(Agent receiver) {
        double closeness = 1;

        if (!foodLocation.equals(receiver.getPosition()))
            closeness = 1 / receiver.environment.topology.getDistance(receiver.getPosition(), foodLocation);

        receiver.setCommInbox(closeness);
    }

}