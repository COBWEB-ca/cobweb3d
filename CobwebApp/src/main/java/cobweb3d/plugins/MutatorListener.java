package cobweb3d.plugins;

import cobweb3d.core.agent.AgentListener;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.agent.ControllerInput;
import cobweb3d.core.entity.Cause;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.plugins.mutators.*;
import cobweb3d.plugins.states.AgentState;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class is the manager of all the mutators used in the simulation.
 * Different mutators are stored in the Sets.
 */
public class MutatorListener implements AgentListener {

    private Set<SpawnMutator> spawnMutators = new LinkedHashSet<>();
    private Set<ContactMutator> contactMutators = new LinkedHashSet<>();
    private Set<StepMutator> stepMutators = new LinkedHashSet<>();
    private Set<EnergyMutator> energyMutators = new LinkedHashSet<>();
    private Set<UpdateMutator> updateMutators = new LinkedHashSet<>();

    private Set<DataLoggingMutator> dataLoggingMutators = new LinkedHashSet<>();
    private Set<ConsumptionMutator> consumptionMutators = new LinkedHashSet<>();
    private Set<ControllerInputMutator> controllerMutators = new LinkedHashSet<>();
    // allMutators keeps track of all mutators that ever existed during the simulation.
    private Set<AgentMutator> allMutators = new HashSet<>();

    /**
     * Based on the given config, remove all the unsupported mutators.
     *
     * @param config configuration file of the mutator listener.
     */
    public void loadConfig(MutatorListenerConfig config) {
        Set<AgentMutator> mutatorsToRemove = new HashSet<>();
        for (AgentMutator agentMutator : allMutators) {
            if (!config.enabled(agentMutator.getClass().getCanonicalName())) {
                mutatorsToRemove.add(agentMutator);
            }
        }
        for (AgentMutator agentMutator : mutatorsToRemove) {
            removeMutator(agentMutator);
            // method "removeMutator" removes the input from all the sets.
            // we have to add the input back to the set allMutators, since it tracks all the mutators that existed.
            allMutators.add(agentMutator);
        }
    }

    public void addMutator(AgentMutator mutator) {
        if (mutator instanceof SpawnMutator)
            spawnMutators.add((SpawnMutator) mutator);

        if (mutator instanceof ContactMutator)
            contactMutators.add((ContactMutator) mutator);

        if (mutator instanceof StepMutator)
            stepMutators.add((StepMutator) mutator);

        if (mutator instanceof EnergyMutator)
            energyMutators.add((EnergyMutator) mutator);

        if (mutator instanceof UpdateMutator)
            updateMutators.add((UpdateMutator) mutator);

        if (mutator instanceof ConsumptionMutator)
            consumptionMutators.add((ConsumptionMutator) mutator);

        if (mutator instanceof DataLoggingMutator)
            dataLoggingMutators.add((DataLoggingMutator) mutator);

        if (mutator instanceof ControllerInputMutator)
            controllerMutators.add((ControllerInputMutator) mutator);


        allMutators.add(mutator);
    }

    /**
     * Remove the given mutator from all the sets which are used to store mutators.
     *
     * @param mutator the mutator that is going to be removed.
     */
    public void removeMutator(AgentMutator mutator) {
        spawnMutators.remove(mutator);
        contactMutators.remove(mutator);
        stepMutators.remove(mutator);
        energyMutators.remove(mutator);
        updateMutators.remove(mutator);
        dataLoggingMutators.remove(mutator);
        consumptionMutators.remove(mutator);
        controllerMutators.remove(mutator);

        allMutators.remove(mutator);
    }

    /**
     * Empty all the sets used to store mutators.
     */
    public void clearMutators() {
        spawnMutators.clear();
        contactMutators.clear();
        stepMutators.clear();
        energyMutators.clear();
        updateMutators.clear();
        dataLoggingMutators.clear();
        consumptionMutators.clear();
        controllerMutators.clear();

        allMutators.clear();
    }

    /**
     * Checks whether given AgentState can be used in the current simulation configuration.
     * Note: In v0.1.4, state system hasn't been completely implemented yet.
     * So this method will always return false.
     *
     * @param type  specific Class of AgentState
     * @param value value of AgentState
     * @param <T> subclass of class AgentState
     * @return true if AgentState supported in current configuration
     */
    public <T extends AgentState> boolean supportsState(Class<T> type, T value) {
        for (AgentMutator mutator : allMutators) {
            if (mutator.acceptsState(type, value))
                return true;
        }

        return false;
    }

    @Override
    public void onContact(BaseAgent bumper, BaseAgent bumpee) {
        for (ContactMutator mut : contactMutators) {
            mut.onContact(bumper, bumpee);
        }
    }

    @Override
    public void onStep(BaseAgent agent, LocationDirection from, LocationDirection to) {
        for (StepMutator m : stepMutators) {
            m.onStep(agent, from, to);
        }
    }

    @Override
    public void onSpawn(BaseAgent agent, BaseAgent parent1, BaseAgent parent2) {
        for (SpawnMutator mutator : spawnMutators) {
            mutator.onSpawn(agent, parent1, parent2);
        }
    }

    @Override
    public void onSpawn(BaseAgent agent, BaseAgent parent) {
        for (SpawnMutator mutator : spawnMutators) {
            mutator.onSpawn(agent, parent);
        }
    }

    @Override
    public void onSpawn(BaseAgent agent) {
        for (SpawnMutator mutator : spawnMutators) {
            mutator.onSpawn(agent);
        }
    }

    @Override
    public void onDeath(BaseAgent agent) {
        for (SpawnMutator mutator : spawnMutators) {
            mutator.onDeath(agent);
        }
    }

    @Override
    public void onConsumeAgent(BaseAgent agent, BaseAgent food) {
        for (ConsumptionMutator mutator : consumptionMutators) {
            mutator.onConsumeAgent(agent, food);
        }
    }

    @Override
    public void onConsumeFood(BaseAgent agent, int foodType) {
        for (ConsumptionMutator mutator : consumptionMutators) {
            mutator.onConsumeFood(agent, foodType);
        }
    }

    @Override
    public void onEnergyChange(BaseAgent agent, int delta, Cause cause) {
        for (EnergyMutator mutator : energyMutators) {
            mutator.onEnergyChange(agent, delta, cause);
        }
    }

    @Override
    public void onUpdate(BaseAgent agent) {
        for (UpdateMutator mutator : updateMutators) {
            mutator.onUpdate(agent);
        }
    }

    @Override
    public void beforeControl(BaseAgent agent, ControllerInput cInput) {
        for (ControllerInputMutator mutator : controllerMutators) {
            mutator.onControl(agent, cInput);
        }
    }

    public Set<DataLoggingMutator> getDataLoggingMutators() {
        return dataLoggingMutators;
    }

    public Set<AgentMutator> getAllMutators() {
        return allMutators;
    }
}
