package cobweb3d.plugins.resources;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.location.Location;
import cobweb3d.impl.Simulation;
import cobweb3d.impl.environment.Environment;
import cobweb3d.plugins.mutators.ConfiguratedMutator;
import cobweb3d.plugins.mutators.EnvironmentMutator;

public class FoodGrowth implements EnvironmentMutator, ConfiguratedMutator<ResourceParams> {

    private static int MAX_ATTEMPTS = 5;

    private int agentTypes;

    public ResourceParams params;

    private SimulationTimeSpace simulation;

    public FoodGrowth() {
    }

    private void dropFood(int type) {
        float foodDrop = params.agentParams[type].dropRate;
        while (simulation.getRandom().nextFloat() < foodDrop) {
            --foodDrop;
            Location l;
            int j = 0;
            do {
                ++j;
                l = simulation.getTopology().getRandomLocation();
            } while (j < MAX_ATTEMPTS && simulation.getEnvironment().isOccupied(l));

            if (j < MAX_ATTEMPTS) {
                simulation.getEnvironment().setFood(l, type);
            }
        }
    }

    private void loadNewFood() {
        Environment env = (Environment) simulation.getEnvironment();

        for (int i = 0; i < agentTypes; i++) {
            for (int j = 0; j < params.agentParams[i].initialFood; j++) {
                Location l;
                int tries = 0;
                do l = simulation.getTopology().getRandomLocation();
                while (tries++ < 100 && env.foodArray[l.x][l.y][l.z] > 0);
                if (tries < 100) {
                    env.setFood(l, i + 1);
                }
            }
        }
    }

    public void loadNew() {
        loadNewFood();
    }

    @Override
    public boolean acceptsParam(Class<?> object) {
        return object.isAssignableFrom(ResourceParams.class);
    }

    @Override
    public void setParams(SimulationTimeSpace sim, ResourceParams resourceParams, int agentTypes) {
        this.simulation = sim;
        this.params = resourceParams;
        this.agentTypes = agentTypes;
    }
}
