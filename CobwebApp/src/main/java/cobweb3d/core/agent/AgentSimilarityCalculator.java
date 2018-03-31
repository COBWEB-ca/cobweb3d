package cobweb3d.core.agent;


/**
 * Calculates similarity between two agents. This is used in combination with minimum communication
 * similarity and minimum breeding similarity.
 */
public interface AgentSimilarityCalculator {

    /**
     * Calculates similarity between given agents.
     *
     * @param a1 First agent.
     * @param a2 Second agent.
     * @return similarity value.
     */
    float similarity(BaseAgent a1, BaseAgent a2);
}
