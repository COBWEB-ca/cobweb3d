package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;

public interface ConsumptionMutator extends AgentMutator {

    /**
     * One agent consumes another agent.
     *
     * @param agent the predator agent.
     * @param food the food agent.
     */
    void onConsumeAgent(BaseAgent agent, BaseAgent food);

    default void onConsumeFood(BaseAgent agent, int foodType) {
    }
}
