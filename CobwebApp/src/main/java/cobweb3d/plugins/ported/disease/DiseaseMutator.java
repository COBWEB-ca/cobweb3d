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

    @Override
    public void onContact(BaseAgent bumper, BaseAgent bumpee) {
        transmitBumpOneWay(bumper, bumpee);
        transmitBumpOneWay(bumpee, bumper);
    }

    @Override
    public void onDeath(BaseAgent agent) {
        DiseaseState diseaseState = removeAgentState(agent);
        if (diseaseState != null && diseaseState.sick)
            sickCount[agent.getType()]--;
    }

    @Override
    public void onSpawn(BaseAgent agent) {
        makeRandomSick(agent, params.agentParams[agent.getType()].initialInfection);
    }

    @Override
    public void onSpawn(BaseAgent agent, BaseAgent parent) {
        if (parent.isAlive() && isSick(parent))
            makeRandomSick(agent, params.agentParams[agent.getType()].childTransmitRate);
        else
            makeRandomSick(agent, 0);
    }

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

    private void vaccinate(BaseAgent bumpee, float effectiveness) {
        DiseaseAgentParams agentParams = params.agentParams[bumpee.getType()];
        setAgentState(bumpee, new DiseaseState(agentParams, false, true, effectiveness));
    }

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
