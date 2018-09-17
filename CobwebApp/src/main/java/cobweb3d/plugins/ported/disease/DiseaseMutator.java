/**
 *
 */
package cobweb3d.plugins.ported.disease;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.plugins.mutators.ContactMutator;
import cobweb3d.plugins.mutators.SpawnMutator;
import cobweb3d.plugins.mutators.StatefulMutatorBase;
import cobweb3d.plugins.mutators.UpdateMutator;
import cobwebutil.ArrayUtilities;

/**
 * Simulates various diseases that can affect agents.
 */
public class DiseaseMutator extends StatefulMutatorBase<DiseaseState, DiseaseParams> implements ContactMutator, SpawnMutator, UpdateMutator {

    private DiseaseParams params;

    private int sickCount[] = new int[0];

    private SimulationTimeSpace simulation;

    public DiseaseMutator() {
        super(DiseaseState.class);
    }

    @Override
    public boolean acceptsParam(Class<?> object) {
        return object.isAssignableFrom(DiseaseParams.class);
    }

    /*
            TODO: Logging
         */

    /**
     * Decide whether or not one agent will be sick now, using random number.
     *
     * @param agent The agent whose health state will be decided.
     * @param rate The probability that this agent will be sick.
     */
    private void makeRandomSick(BaseAgent agent, float rate) {
        boolean isSick = false;
        if (simulation.getRandom().nextFloat() < rate)
            isSick = true;

        if (isSick) {
            DiseaseAgentParams agentParams = params.agentParams[agent.getType()];
            agentParams.param.modifyValue(this, agent, agentParams.factor);

            sickCount[agent.getType()]++;

            setAgentState(agent, new DiseaseState(agentParams, true, false, simulation.getTime()));
        }

    }

    /**
     * A listener for two agents who meet each other.
     *
     * @param bumper BaseAgent that moved to make contact.
     * @param bumpee BaseAgent that got bumped into by the other.
     */
    @Override
    public void onContact(BaseAgent bumper, BaseAgent bumpee) {
        transmitBumpOneWay(bumper, bumpee);
        transmitBumpOneWay(bumpee, bumper);
    }

    /**
     * When one agent dies, the sickCount would decrease one if the agent was sick.
     *
     * @param agent BaseAgent that died.
     */
    @Override
    public void onDeath(BaseAgent agent) {
        DiseaseState diseaseState = removeAgentState(agent);
        if (diseaseState != null && diseaseState.sick)
            sickCount[agent.getType()]--;
    }

    /**
     * This listener method is for agents who are created at the beginning of the simulation (No parents).
     * Their sick probability is decided by their parameters.
     *
     * @param agent BaseAgent spawned.
     */
    @Override
    public void onSpawn(BaseAgent agent) {
        makeRandomSick(agent, params.agentParams[agent.getType()].initialInfection);
    }

    /**
     * Decide whether or not the baby agent would be sick, based on whether or not its parent is sick.
     *
     * @param agent  BaseAgent produced.
     * @param parent Asexual parent.
     */
    @Override
    public void onSpawn(BaseAgent agent, BaseAgent parent) {
        if (parent.isAlive() && isSick(parent))
            makeRandomSick(agent, params.agentParams[agent.getType()].childTransmitRate);
        else
            makeRandomSick(agent, 0);
    }

    /**
     * Decide whether or not the baby agent would be sick, based on whether or not its parents are sick.
     *
     * @param agent   BaseAgent produced.
     * @param parent1 First parent.
     * @param parent2 Second parent.
     */
    @Override
    public void onSpawn(BaseAgent agent, BaseAgent parent1, BaseAgent parent2) {
        if ((parent1.isAlive() && isSick(parent1)) || (parent2.isAlive() && isSick(parent2)))
            makeRandomSick(agent, params.agentParams[agent.getType()].childTransmitRate);
        else
            makeRandomSick(agent, 0);
    }

    @Override
    public void setParams(SimulationTimeSpace sim, DiseaseParams diseaseParams, int agentTypes) {
        this.simulation = sim;
        this.params = diseaseParams;
        sickCount = ArrayUtilities.resizeArray(sickCount, agentTypes);
    }

    /**
     * Called when one agent bumps another. The disease of each agent may infect the other one.
     * Similarly, the vaccinated agent may vaccinate the other agent.
     *
     * @param bumper The agent who bumps the other agent.
     * @param bumpee The agent who is bumped by the other agent.
     */
    private void transmitBumpOneWay(BaseAgent bumper, BaseAgent bumpee) {
        int tr = bumper.getType();
        int te = bumpee.getType();

        if (params.agentParams[tr].vaccinator && !isSick(bumpee)) {
            vaccinate(bumpee, params.agentParams[tr].vaccineEffectiveness);
        }

        if (params.agentParams[tr].healer && isSick(bumpee)) {
            if (simulation.getRandom().nextFloat() < params.agentParams[tr].healerEffectiveness) {
                unSick(bumpee);
            }
        }

        if (!isSick(bumper))
            return;

        if (isVaccinated(bumpee)
                && simulation.getRandom().nextFloat() < getAgentState(bumpee).vaccineEffectiveness)
            return;

        if (isSick(bumpee))
            return;

        if (params.agentParams[tr].transmitTo[te]) {
            makeRandomSick(bumpee, params.agentParams[te].contactTransmitRate);
        }
    }

    private void unSick(BaseAgent agent) {
        removeAgentState(agent);
        sickCount[agent.getType()]--;
    }

    public boolean isSick(BaseAgent agent) {
        return hasAgentState(agent) && getAgentState(agent).sick;
    }

    private boolean isVaccinated(BaseAgent agent) {
        return hasAgentState(agent) && getAgentState(agent).vaccinated;
    }

    /**
     * Vaccinate one agent, which decrease its probability of getting sick.
     *
     * @param bumpee The agent that is going to be vaccinated.
     * @param effectiveness The effectiveness of the vaccine.
     */
    private void vaccinate(BaseAgent bumpee, float effectiveness) {
        DiseaseAgentParams agentParams = params.agentParams[bumpee.getType()];
        setAgentState(bumpee, new DiseaseState(agentParams, false, true, effectiveness));
    }

    /**
     * Called when the agent is updated. The sick agent may be healthy again based on random number.
     *
     * @param a The agent who is going to be updated.
     */
    @Override
    public void onUpdate(BaseAgent a) {
        if (!hasAgentState(a))
            return;

        DiseaseState s = getAgentState(a);

        if (params.agentParams[a.getType()].recoveryTime == 0)
            return;

        long randomRecovery = (long) (params.agentParams[a.getType()].recoveryTime * (simulation.getRandom().nextDouble() * 0.2 + 1.0));

        if (s.sick && simulation.getTime() - s.sickStart > randomRecovery) {
            unSick(a);
        }
    }

    @Override
    protected boolean validState(DiseaseState value) {
        return value != null;
    }

}
