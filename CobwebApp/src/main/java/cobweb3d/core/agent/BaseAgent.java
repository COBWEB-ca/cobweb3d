package cobweb3d.core.agent;


import cobweb3d.core.Updatable;
import cobweb3d.core.entity.Cause;
import cobweb3d.core.location.LocationDirection;

/**
 * Basic properties of an BaseAgent
 */
public abstract class BaseAgent implements Updatable {
    public LocationDirection position;
    private Integer id;
    private int type;
    private boolean alive = true;
    private int energy;

    public BaseAgent(int type) {
        this.type = type;
    }

    public int id() {
        return id;
    }

    public void setId(Integer id) {
        if (this.id != null)
            throw new IllegalStateException("BaseAgent id is already set for " + this.toString() + "!");
        else this.id = id;
    }

    public void die() {
        if (!isAlive())
            return;
        alive = false;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean enoughEnergy(int required) {
        return getEnergy() >= required;
    }

    /**
     * Changes the agent's energy level.
     *
     * @param delta Energy change delta, positive means agent gains energy, negative means it loses
     */
    public void changeEnergy(int delta) {
        changeEnergy(delta, null);
    }

    /**
     * Changes the agent's energy level.
     *
     * @param delta Energy change delta, positive means agent gains energy, negative means it loses
     * @param cause Why the energy changed.
     */
    public void changeEnergy(int delta, Cause cause) {
        energy += delta;
    }

    /**
     * @return the location this BaseAgent occupies.
     */
    public LocationDirection getPosition() {
        return position;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getType() {
        return type;
    }

    public void transformType(int type) {
        this.type = type;
    }

    public abstract BaseAgent createChildAsexual(LocationDirection location);

    public abstract BaseAgent createChildSexual(LocationDirection location, BaseAgent otherParent);
}
