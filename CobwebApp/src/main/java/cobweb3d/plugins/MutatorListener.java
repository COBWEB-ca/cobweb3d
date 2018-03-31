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


public class MutatorListener implements AgentListener {

    private Set<SpawnMutator> spawnMutators = new LinkedHashSet<>();
    private Set<ContactMutator> contactMutators = new LinkedHashSet<>();
    private Set<StepMutator> stepMutators = new LinkedHashSet<>();
    private Set<EnergyMutator> energyMutators = new LinkedHashSet<>();
    private Set<UpdateMutator> updateMutators = new LinkedHashSet<>();

    private Set<DataLoggingMutator> dataLoggingMutators = new LinkedHashSet<>();
    private Set<ConsumptionMutator> consumptionMutators = new LinkedHashSet<>();
    private Set<ControllerInputMutator> controllerMutators = new LinkedHashSet<>();
    private Set<AgentMutator> allMutators = new HashSet<>();

    public void loadConfig(MutatorListenerConfig config) {
        Set<AgentMutator> mutatorsToRemove = new HashSet<>();
        for (AgentMutator agentMutator : allMutators) {
            if (!config.enabled(agentMutator.getClass().getCanonicalName())) {
                mutatorsToRemove.add(agentMutator);
            }
        }
        for (AgentMutator agentMutator : mutatorsToRemove) {
            removeMutator(agentMutator);
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
