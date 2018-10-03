package cobweb3d.core.environment;

import cobweb3d.impl.Simulation;
import cobweb3d.core.SimulationInternals;
import cobweb3d.core.Updatable;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.location.Location;
import cobweb3d.core.params.BaseEnvironmentParams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The 3D environment where the simulation happens in.
 * It uses a hash table to link one location to one agent who stays in that location.
 */
public class BaseEnvironment implements Updatable {
    public BaseEnvironmentParams environmentParams;

    public Topology topology;
    public int FLAG_AGENT = 0x01;
    public int FLAG_STONE = 0x02;
    protected SimulationInternals simulation;
    /**
     * The implementation uses a hash table to store agents, as we assume there are many more locations than agents.
     */
    protected Map<Location, BaseAgent> agentTable;

    /**
     * flagArray is a 3d array where each entry represents the current state(?) in that position.
     * It is still unused yet (v0.1.4)
     */
    protected byte[][][] flagArray; // TODO

    /**
     * foodArray is a 3d array where each entry is whether there is a food on the tile
     */
    public byte[][][] foodArray;

    public BaseEnvironment(SimulationInternals simulation) {
        this.simulation = simulation;
        this.agentTable = new ConcurrentHashMap<>();
        flagArray = new byte[0][0][0];
        foodArray = new byte[0][0][0];
    }

    public synchronized void setParams(BaseEnvironmentParams envParams, boolean keepOldAgents) throws IllegalArgumentException {
        this.environmentParams = envParams;
        this.topology = new Topology(simulation, environmentParams.width, environmentParams.height, envParams.depth, false);
        this.flagArray = new byte[topology.width][topology.height][topology.depth];
        this.foodArray = new byte[topology.width][topology.height][topology.depth];
    }

    private byte getLocationBits(Location l) {
        return flagArray[l.x][l.y][l.z];
    }

    private void setLocationBits(Location l, byte bits) {
        flagArray[l.x][l.y][l.z] = bits;
    }

    /**
     * Flags locations as a food/stone/waste location. It does nothing if
     * the square is already occupied (for example, setFlag((0,0),FOOD,true)
     * does nothing when (0,0) is a stone
     */
    protected void setFlag(Location l, byte flag, boolean state) {
        int flagBits = 1 << (flag - 1);

        assert (!(state && getLocationBits(l) != 0)) : "Attempted to set flag when location flags non-zero: " + getLocationBits(l);
        assert (!(!state && (getLocationBits(l) & flagBits) == 0)) : "Attempting to unset an unset flag" + flagBits;

        byte newValue = getLocationBits(l);

        if (state)
            newValue |= flagBits;
        else
            newValue &= ~flagBits;

        setLocationBits(l, newValue);
    }

    protected boolean testFlag(Location l, byte flag) {
        int flagBits = 1 << (flag - 1);
        return (getLocationBits(l) & flagBits) != 0;
    }

    protected void clearFlag(byte flag) {
        for (int x = 0; x < topology.width; ++x) {
            for (int y = 0; y < topology.height; ++y) {
                for (int z = 0; z < topology.depth; ++z) {
                    Location currentPos = new Location(x, y, z);
                    if (testFlag(currentPos, flag)) {
                        setFlag(currentPos, flag, false);
                    }
                }
            }
        }
    }

    public boolean isOccupied(Location l) {
        return getLocationBits(l) != 0 || hasAgent(l);
    }

    public boolean hasAgent(Location l) {
        if (l == null) return false;
        if (agentTable != null && agentTable.containsKey(l)) {
            BaseAgent agent = agentTable.get(l);
            if (!agent.isAlive()) {
                agentTable.remove(l);
                return false;
            } else return true;
        } else return false;
    }

    public void clearAgents() {
        for (BaseAgent a : new ArrayList<>(getAgents())) {
            a.die();
        }
        agentTable.clear();
    }

    public BaseAgent getAgent(Location l) {
        return agentTable.get(l);
    }

    public Collection<BaseAgent> getAgents() {
        return agentTable.values();
    }

    public int getAgentCount() {
        return agentTable.size();
    }

    public final void setAgent(Location l, BaseAgent a) {
        if (a != null) agentTable.put(l, a);
        else agentTable.remove(l);
    }

    public synchronized void removeAgent(Location l) {
        BaseAgent a = getAgent(l);
        if (a != null) a.die();
    }

    public final void setFood(Location l, int foodType) {
        if (1 <= foodType && foodType <= ((Simulation) simulation).getAgentTypeCount()) {
            foodArray[l.x][l.y][l.z] = (byte) foodType;
        }
    }

    public synchronized  void removeFood(Location l) {
        foodArray[l.x][l.y][l.z] = 0;
    }

    protected void killOffgridAgents() {
        for (BaseAgent a : new ArrayList<>(getAgents())) {
            Location l = a.getPosition();
            if (l.x >= topology.width || l.x < 0 || l.y >= topology.height || l.y < 0 || l.z >= topology.depth || l.z < 0) {
                a.die();
            }
        }
    }
}
