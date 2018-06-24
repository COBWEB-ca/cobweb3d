package cobweb3d.core.agent;

import cobweb3d.core.entity.Cause;
import cobweb3d.core.location.LocationDirection;

/**
 * A listener class which reacts to
 *  1. An agent meets another agent.
 *  2. An agent makes a move.
 *  3. An baby agent spawns.
 *  4. An agent dies.
 *  5. An agent eats some food. (unused in v0.1.4)
 *  6. An agent hunts another agent.
 *  7. The energy of one agent is changed. (unused in v0.1.4)
 *  8. An agent updates.
 *
 * Document Author: Zewen Shen
 * Reminder: I'm not the author of this class, so my document may be inaccurate.
 */
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
