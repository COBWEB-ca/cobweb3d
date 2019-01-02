package cobweb3d.plugins.broadcast;

import cobweb3d.core.entity.Cause;
import cobweb3d.core.environment.Topology;
import cobweb3d.core.location.Location;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.mutators.EnvironmentMutator;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class PacketConduit implements EnvironmentMutator {

    private boolean broadcastBlocked = false;

    private Collection<BroadcastPacket> currentPackets = new LinkedList<BroadcastPacket>();

    private Topology topology;

    public void setParams(Topology topo) {
        this.topology = topo;
    }

    /**
     * adds packets to the list of packets
     *
     * @param packet packet
     */
    public void addPacketToList(BroadcastPacket packet) {
        if (!broadcastBlocked)
            currentPackets.add(packet);
        // TODO: allow more brodcasts?
        blockBroadcast();
    }

    public void blockBroadcast() {
        broadcastBlocked = true;
    }

    // with every time step, the persistence of the packets should be
    // decremented
    public void decrementPersistence() {
        Iterator<BroadcastPacket> i = currentPackets.iterator();
        while (i.hasNext()) {
            BroadcastPacket packet = i.next();
            if (!packet.updateCheckActive())
                i.remove();
        }
    }

    public void unblockBroadcast() {
        broadcastBlocked = false;
    }

    public void clearPackets() {
        currentPackets.clear();
    }

    public BroadcastPacket findPacket(Location position, Agent receiver) {
        // TODO: return more than 1 packet?
        // TODO: return closest packet?
        for (BroadcastPacket commPacket : currentPackets) {
            double distance = topology.getDistance(position, commPacket.location);
            Agent s = commPacket.sender;
            if (distance < commPacket.range
                    && !s.equals(receiver)
                    && (!s.params.broadcastSameTypeOnly || receiver.getType() == s.getType())
                    ) {
                return commPacket;
            }
        }
        return null;
    }

    @Override
    public void update() {
        decrementPersistence();
        unblockBroadcast();
    }

    @Override
    public void loadNew() {

    }

    public static class BroadcastCause implements Cause {
        @Override
        public String getName() { return "Broadcast"; }
    }

    public static class BroadcastFoodCause extends BroadcastCause {
        @Override
        public String getName() { return "Broadcast Food"; }
    }



}