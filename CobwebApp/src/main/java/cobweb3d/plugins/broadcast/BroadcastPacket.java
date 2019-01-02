package cobweb3d.plugins.broadcast;


import cobweb3d.core.location.Location;
import cobweb3d.impl.agent.Agent;

public abstract class BroadcastPacket {

    public final Agent sender;
    // According to this sender ID, other members may decide whether or not
    // to accept this message

    // e.g. Food found at location 34,43
    public final int range; // Reach over the whole environment or just a
    // certain neighborhood

    public final Location location;

    public BroadcastPacket(Agent dispatcherId) {
        this.sender = dispatcherId;
        this.location = dispatcherId.getPosition();

        if (sender.params.broadcastEnergyBased)
            this.range = getRadius(sender.getEnergy());
        else
            this.range = sender.params.broadcastFixedRange.getValue();
    }

    private static int getRadius(int energy) {
        return energy / 10 + 1; // limiting minimum to 1 unit of
        // radius
    }

    public abstract void process(Agent receiver);

    private int persistence = 1;

    public boolean updateCheckActive() {
        return persistence-- >= 0;
    }
}