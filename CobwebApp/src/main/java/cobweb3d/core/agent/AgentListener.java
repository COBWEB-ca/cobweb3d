package cobweb3d.core.agent;

import cobweb3d.core.entity.Cause;
import cobweb3d.core.location.LocationDirection;

public interface AgentListener extends ControllerListener {

    void onContact(BaseAgent bumper, BaseAgent bumpee);

    void onStep(BaseAgent agent, LocationDirection from, LocationDirection to);

    void onSpawn(BaseAgent agent, BaseAgent parent1, BaseAgent parent2);

    void onSpawn(BaseAgent agent, BaseAgent parent);

    void onSpawn(BaseAgent agent);

    void onDeath(BaseAgent agent);

    void onConsumeFood(BaseAgent agent, int foodType);

    void onConsumeAgent(BaseAgent agent, BaseAgent food);

    void onEnergyChange(BaseAgent agent, int delta, Cause cause);

    void onUpdate(BaseAgent agent);
}
