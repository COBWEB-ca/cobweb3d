package cobweb3d.plugins.reproduction;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.location.Location;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.mutators.ContactMutator;
import cobweb3d.plugins.mutators.StatefulMutatorBase;
import cobweb3d.plugins.mutators.StepMutator;
import cobweb3d.plugins.mutators.UpdateMutator;
import cobwebutil.ArrayUtilities;

public class ReproductionMutator extends StatefulMutatorBase<ReproductionState, ReproductionParams> implements ContactMutator,
        StepMutator, UpdateMutator {
    ReproductionParams params;

    private int[] birthCounts = new int[0];

    private SimulationTimeSpace simulation;

    public ReproductionMutator() {
        super(ReproductionState.class);
    }

    @Override
    public void setParams(SimulationTimeSpace sim, ReproductionParams reproductionParams, int agentTypes) {
        this.simulation = sim;
        this.params = reproductionParams;
        this.birthCounts = ArrayUtilities.resizeArray(birthCounts, agentTypes);
    }

    @Override
    public boolean acceptsParam(Class<?> object) {
        return object.isAssignableFrom(ReproductionParams.class);
    }

    /**
     * This method is called when one agent is trying to give birth.
     *
     * @param agent The agent who is trying to give birth.
     * @param location The location where the child will be born in.
     */
    private void tryGiveBirth(BaseAgent agent, Location location) {
        // Verify whether or not the agent has enough energy to give birth && its pregnancy period is over
        if (agent.enoughEnergy(params.of(agent).breedEnergy.getValue()) && getPregnancyPeriod(agent) <= 0) {
            LocationDirection breedPos = new LocationDirection(location, agent.getPosition().direction);
            BaseAgent breedPartner;
            if ((breedPartner = getBreedPartner(agent)) != null) {
                agent.createChildSexual(breedPos, breedPartner);
                setBreedPartner(agent, null);
            } else agent.createChildAsexual(breedPos);
            setPregnant(agent, false);
            if (agent instanceof Agent) {
                agent.changeEnergy(params.of(agent).breedEnergy.getValue(), new Agent.ReproductionCause());
            }
        }
    }

    /**
     * One agent is trying to breed asexually. If succeed, it will be in pregnant state.
     *
     * @param agent The mother agent who is trying to breed asexually.
     */
    private void tryAsexualBreed(BaseAgent agent) {
        int breedEnergy = params.of(agent).breedEnergy.getValue();
        // Decide whether or not this breed is successful by probability.
        if ((breedEnergy == 0 || agent.enoughEnergy(breedEnergy)) && params.of(agent).asexualBreedChance.getValue() != 0.0
                && simulation.getRandom().nextFloat() < params.of(agent).asexualBreedChance.getValue()) {
            setAgentState(agent, ReproductionState.makePregnantState(params.of(agent).asexPregnancyPeriod.getValue(), null));
        }
    }

    /**
     * Two agents are trying to breed sexually. If succeed, the mother will be in pregnant state.
     *
     * @param mother The mother agent who is trying to breed.
     * @param father The father agent who is trying to breed.
     */
    private void trySexualBreed(BaseAgent mother, BaseAgent father) {
        if (mother.getType() == father.getType()) {
            // Decide whether or not this breed is successful by probability.
            // TODO: double sim = 0.0;
            boolean canBreed = !isPregnant(mother) && mother.enoughEnergy(params.of(mother).breedEnergy.getValue()) && params.of(mother).sexualBreedChance.getValue() != 0.0
                    && simulation.getRandom().nextFloat() < params.of(mother).sexualBreedChance.getValue();
            // TODO: Calculate Similarity sim = bumper, check if agent good.
            if (canBreed)
                setAgentState(mother, ReproductionState.makePregnantState(params.of(mother).asexPregnancyPeriod.getValue(), father));
        }
    }

    public boolean isPregnant(BaseAgent agent) {
        return hasAgentState(agent) && getAgentState(agent).pregnant;
    }

    public int getPregnancyPeriod(BaseAgent agent) {
        return hasAgentState(agent) ? getAgentState(agent).pregPeriod : 0;
    }

    public BaseAgent getBreedPartner(BaseAgent agent) {
        return hasAgentState(agent) ? getAgentState(agent).breedPartner : null;
    }

    public void setPregnant(BaseAgent agent, boolean isPregnant) {
        if (hasAgentState(agent)) getAgentState(agent).pregnant = isPregnant;
    }

    public void setPregnancyPeriod(BaseAgent agent, int pregnancyPeriod) {
        if (hasAgentState(agent)) getAgentState(agent).pregPeriod = pregnancyPeriod;
    }

    public void setBreedPartner(BaseAgent agent, BaseAgent breedPartner) {
        if (hasAgentState(agent)) getAgentState(agent).breedPartner = breedPartner;
    }

    @Override
    protected boolean validState(ReproductionState value) {
        return value != null;
    }

    @Override
    public void onContact(BaseAgent bumper, BaseAgent bumpee) {
        trySexualBreed(bumper, bumpee);
    }

    /**
     * On each step of the pregnant mother agent, she will try to give birth in the location where she moves from.
     *
     * @param agent BaseAgent in question.
     * @param from  Old Location. Null if agent is being placed in the environment
     * @param to    New location. Null if agent is being removed from the environment
     */
    @Override
    public void onStep(BaseAgent agent, Location from, Location to) {
        if (isPregnant(agent)) tryGiveBirth(agent, from);
    }

    /**
     * Once the agent is updated, her pregPeriod will minus one.
     * If she is not pregnant, she will try to breed asexually.
     *
     * @param agent the agent to update.
     */
    @Override
    public void onUpdate(BaseAgent agent) {
        if (isPregnant(agent)) getAgentState(agent).pregPeriod--;
        else tryAsexualBreed(agent);
    }
}
