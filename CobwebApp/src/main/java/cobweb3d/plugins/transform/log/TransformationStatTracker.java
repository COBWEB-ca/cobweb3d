package cobweb3d.plugins.transform.log;

public class TransformationStatTracker {

   /* public static float getAverageSpeed(BaseStatsProvider statsProvider) {
        long totalPop = statsProvider.getAgentCount();
        float totalAvgSpd = 0;
        for (BaseAgent a : statsProvider.getAgents()) {
            if (a instanceof Agent) {
                TransformationState state = ((Agent) a).getState(TransformationState.class);
                if (state != null) {
                    totalAvgSpd += state.avgTransformSpeed;
                }
            }
        }
        return totalPop == 0 ? 0 : (totalAvgSpd/(float)totalPop);
    }

    public static float getAverageSpeedForAgent(BaseStatsProvider statsProvider, int type) {
        long pop = statsProvider.countAgents(type);
        float totalAvgSpd = 0;
        for (BaseAgent a : statsProvider.getAgents()) {
            if (a.getType() == type && a instanceof Agent) {
                TransformationState state = ((Agent) a).getState(TransformationState.class);
                if (state != null) {
                    totalAvgSpd += state.avgTransformSpeed;//exchangeParams.getAgentParams(a).calculateU(state);
                }
            }
        }
        return pop == 0 ? 0 : ((float)totalAvgSpd/(float)pop);
    }*/
}
