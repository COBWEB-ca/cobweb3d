package cobweb3d.plugins.resources;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.location.Direction;
import cobweb3d.core.location.Location;
import cobweb3d.impl.Simulation;
import cobweb3d.impl.environment.Environment;
import cobweb3d.plugins.mutators.ConfiguratedMutator;
import cobweb3d.plugins.mutators.EnvironmentMutator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FoodGrowth implements EnvironmentMutator, ConfiguratedMutator<ResourceParams> {

    private static int MAX_ATTEMPTS = 5;

    private int agentTypes;

    public ResourceParams params;

    public Environment env;

    private SimulationTimeSpace simulation;

    public FoodGrowth() {
    }

    public void depleteFood() {
        // for each agent type, we test to see if its deplete time step has
        // come, and if so deplete the food random
        // by the appropriate percentage

        for (int type = 0; type < agentTypes; type++) {
            ResourceAgentParams p = params.agentParams[type];

            if (p.depleteRate != 0.0f &&
                    p.growRate > 0 &&
                    simulation.getTime() != 0 &&
                    (simulation.getTime() % p.depleteTime) == 0) {
                depleteFood(p, type + 1);
            }
        }
    }

    public void depleteFood(ResourceAgentParams p, int type) {
        List<Location> locations = new ArrayList<>();
        for (int x = 0; x < simulation.getTopology().width; ++x) {
            for (int y = 0; y < simulation.getTopology().height; ++y) {
                for (int z = 0; z < simulation.getTopology().depth; ++z) {
                    Location currentPos = new Location(x, y, z);
                    if (env.hasFood(currentPos) && env.getFoodType(currentPos) == type) {
                        locations.add(currentPos);
                    }
                }
            }
        }

        Collections.shuffle(locations, simulation.getRandom());

        int foodToDeplete = (int) (locations.size() * p.depleteRate);

        for (int j = 0; j < foodToDeplete; ++j) {
            Location loc = locations.get(j);
            env.removeFood(loc);
        }
    }

    private void growFood() {
        for (int x = 0; x < simulation.getTopology().width; x++ ) {
            for (int y = 0; y < simulation.getTopology().height; y++) {
                for (int z = 0; z < simulation.getTopology().depth; z++) {
                    Location currentPos = new Location(x, y, z);
                    if (!env.hasFood(currentPos)) {
                        double foodCount = 0;
                        int[] mostFood = new int[agentTypes + 1];

                        for (Direction dir: Direction.XYZDirs) {
                            Location checkPos = simulation.getTopology().getAdjacent(currentPos, dir);
                            if (checkPos != null && env.hasFood(checkPos)) {
                                foodCount++;
                                mostFood[env.getFoodType(checkPos)]++;
                            }
                        }

                        // If food is found, may want to grow food into this Location
                        if (foodCount > 0) {
                            int max = 0;
                            int growingType;

                            for (int i = 1; i < mostFood.length; ++i)
                                if (mostFood[i] > mostFood[max])
                                    max = i;

                            if (params.agentParams[max-1].likeFoodGrowth >= simulation.getRandom().nextFloat()) {
                                growingType = max;
                            } else {
                                growingType = simulation.getRandom().nextIntRange(1, agentTypes + 1);
                                if (mostFood[growingType] == 0) continue;
                            }

                            // assert growingType > 0;

                            ResourceAgentParams thisType = params.agentParams[growingType -1];
                            float growRate = thisType.growRate;
                            if (foodCount * growRate > 1000 * simulation.getRandom().nextFloat()) {
                                env.setFood(currentPos, growingType);
                            }
                        }
                    }
                }
            }
        }
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
            } while (j < MAX_ATTEMPTS && env.isOccupied(l));

            if (j < MAX_ATTEMPTS) {
                env.setFood(l, type);
            }
        }
    }

    private void loadNewFood() {
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

    @Override
    public void update() {
        depleteFood();

        for (int i = 0; i < params.agentParams.length; i++) {
            // TODO: Drought days?
            dropFood(i);
        }

        boolean shouldGrow = false;
        for (ResourceAgentParams f : params.agentParams) {
            if (f.growRate > 0) {
                shouldGrow = true;
                break;
            }
        }

        if (shouldGrow) {
            growFood();
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
        this.env = (Environment) simulation.getEnvironment();
    }
}
