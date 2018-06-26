package cobweb3d.impl;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.agent.AgentListener;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.environment.Topology;
import cobweb3d.core.location.Location;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.impl.agent.Agent;
import cobweb3d.impl.environment.Environment;
import cobweb3d.plugins.MutatorListener;
import cobweb3d.plugins.PluginProvider;
import cobweb3d.plugins.states.AgentState;
import cobweb3d.ui.SimulationInterface;
import cobwebutil.RandomNoGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Simulation implements SimulationInternals, SimulationInterface {

    public SimulationConfig simulationConfig;
    public Environment environment;
    private RandomNoGenerator random;
    private long time = 0;
    private int mNextAgentId = 0;
    public MutatorListener mutatorListener = new MutatorListener();
    private volatile List<BaseAgent> mAgents;

    public Simulation() {
        environment = new Environment(this);
        mAgents = new LinkedList<>();
    }

    @Override
    public long getTime() {
        return time;
    }

    public void resetTime() {
        time = 0;
    }

    @Override
    public RandomNoGenerator getRandom() {
        return random;
    }

    @Override
    public Topology getTopology() {
        return environment != null ? environment.topology : null;
    }

    public synchronized Agent addAgent(Location location, int agentType) {
        if (environment.isOccupied(location)) return null;
        else return spawnAgent(location, agentType);
    }

    @Override
    public void registerAgent(BaseAgent agent) {
        mAgents.add(agent);
        agent.setId(mNextAgentId++);
    }

    /**
     * For each type of agents, create "environment.agentPrams[i].initialAgents" (This is a number)
     * agent of that type at random location of this environment.
     * If it can't retrieve a random unoccupied location for 100 times, then this agent can't be spawned.
     * So no agents will be added into the environment once all the locations are occupied.
     */
    public void loadNewAgents() {
        for (int i = 0; i < environment.agentParams.length; i++) {
            for (int j = 0; j < environment.agentParams[i].initialAgents; j++) {
                Location location;
                int tries = 0;
                do location = getTopology().getRandomLocation();
                while (tries++ < 100 & environment.isOccupied(location));
                if (tries < 100) spawnAgent(location, i);
            }
        }
    }

    // TODO: Check
    private static final AtomicLong ticks = new AtomicLong();

    @Override
    public BaseAgent newAgent(int type) {
        return new Agent(this, type);
    }

    public void load(SimulationConfig simConfig) {
        random = simConfig.randomSeed == 0 ? new RandomNoGenerator() : new RandomNoGenerator(simConfig.randomSeed);
        simulationConfig = simConfig;
        environment.setParams(simConfig.envParams, simConfig.agentParams, simulationConfig.keepOldAgents);

        // This if statement deals with the cases where you load a new simConfig when you have already done
        // some simulation using cobweb. You can choose to continue with the existing simulation, or restart a new one.
        if (!simConfig.isContinuation()) {
            mAgents.clear();
            mNextAgentId = 0;
            PluginProvider.constructPlugins(this);
        }

        // TODO: ? time = 0;
        PluginProvider.loadPluginConfigs(this, simulationConfig);

        mutatorListener.loadConfig(simulationConfig.mutatorConfig);
        simulationConfig.logConfig.load(mutatorListener);
        if (simConfig.spawnNewAgents) loadNewAgents();
    }

    @Override
    public int getAgentTypeCount() {
        return simulationConfig != null ? simulationConfig.getAgentTypes() : 0;
    }

    /**
     * This method will be executed once every tick, in order to update environment and agents.
     * mutatorListener will be notified of changes of agents after agents get updated.
     *
     * Using "synchronized" to make sure that no other thread can execute the code block inside
     * "synchronized (ticks)".
     */
    @Override
    public void step() {
        environment.update();
        synchronized (ticks) {
            for (BaseAgent agent : new LinkedList<>(mAgents)) {
                agent.update();
                mutatorListener.onUpdate(agent);
                if (!agent.isAlive()) mAgents.remove(agent);
            }
        }
        time++;
        ticks.set(time);
    }

    public Agent spawnAgent(Location location, int agentType) {
        Agent agent = (Agent) newAgent(agentType);
        agent.init(environment, new LocationDirection(location, getTopology().getRandomDirection()),
                environment.agentParams[agentType], simulationConfig.controllerParams.createController(this, agentType));
        return agent;
    }

    @Override
    public AgentListener getAgentListener() {
        return mutatorListener;
    }

    /**
     * Checks whether given AgentState can be used in the current simulation configuration
     *
     * @param type  specific Class of AgentState
     * @param value value of AgentState
     * @return true if AgentState supported in current configuration
     */
    public <T extends AgentState> boolean supportsState(Class<T> type, T value) {
        return mutatorListener.supportsState(type, value);
    }

    public List<BaseAgent> getAgents() {
        return mAgents;
    }
}
