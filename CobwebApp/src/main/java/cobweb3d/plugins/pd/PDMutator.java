package cobweb3d.plugins.pd;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.mutators.ContactMutator;
import cobweb3d.plugins.mutators.StatefulSpawnMutatorBase;

public class PDMutator extends StatefulSpawnMutatorBase<PDState> implements ContactMutator {

    SimulationInternals sim;
    PDParams params;

    public PDMutator(SimulationInternals sim) {
        super(PDState.class, sim);
        this.sim = sim;
    }

    public void setParams(PDParams pdParams) {
        this.params = pdParams;
    }

    @Override
    public PDState stateForNewAgent(BaseAgent agent) {
        if (!params.enable || !params.agentParams[agent.getType()].pdEnabled)
            return null;

        return new PDState(params.agentParams[agent.getType()].clone());
    }

    @Override
    protected PDState stateFromParent(BaseAgent agent, PDState parentState) {
        if (!params.enable || !params.agentParams[agent.getType()].pdEnabled)
            return null;

        return new PDState(parentState.agentParams.clone());
    }

    @Override
    public void onContact(BaseAgent bumper, BaseAgent bumpee) {
        Agent me = (Agent) bumper;
        if (!hasAgentState(me))
            return;

        Agent other = (Agent) bumpee;
        if (!hasAgentState(other))
            return;

        if (me.isAgentGood(other) && other.isAgentGood(me)) {
            playPDonStep(me, other);
        }
    }



    /**
     * This method initializes the agents actions in an iterated prisoner's
     * dilemma game.  The agent can use the following strategies described
     * by the agentPDStrategy integer:
     *
     * <p>0. Default
     *
     * <p>The agents decision to defect or cooperate is chosen randomly.
     * The probability of choosing either is determined by the agents
     * pdCoopProb parameter.
     *
     * <p>1. Tit for Tat
     *
     * <p>The agent will initially begin with a cooperate, but will then choose
     * whatever the opposing agent chose last.  For example, the agent begins
     * with a cooperate, but if the opposing agent has chosen to defect, then
     * the agent will choose to defect next round.
     *
     */
    public boolean playPD(BaseAgent me, PDState meState, BaseAgent other) {

        double coopProb = meState.agentParams.pdCoopProb.getValue() / 100.0d;

        float similarity = sim.getSimilarityCalculator().similarity(me, other);

        coopProb += (similarity - meState.agentParams.pdSimilarityNeutral.getValue()) * meState.agentParams.pdSimilaritySlope.getValue();

        if (meState.agentParams.pdTitForTat) { // if true then agent is playing TitForTat
            meState.pdCheater = meState.lastPDcheated;
        } else {
            meState.pdCheater = false; // agent is assumed to cooperate
            float rnd = sim.getRandom().nextFloat();
            if (rnd > coopProb)
                meState.pdCheater = true; // agent defects depending on probability
        }

        return meState.pdCheater;
    }


    /**
     *Prisoner's dilemma is played between the two agents using the strategies
     *assigned in playPD().  The agent will use its PD memory to remember agents
     *that cheat it, which will affect whether an agent will want to meet another,
     *and its credibility.
     *
     *<p>How Prisoner's Dilemma is played:
     *
     *<p>Prisoner's dilemma is a game between two agents when they come in to
     *contact with each other.  The game determines how much energy each agent
     *receives after contact.  Each agent has two options: cooperate or defect.
     *The agents choice to cooperate or defect is determined by the strategy the
     *agent is using (see playPD() method).  The agents choices can lead to
     *one of four outcomes:
     *
     *<p> 1. REWARD for mutual cooperation (Both agents cooperate)
     *
     *<p> 2. SUCKER's payoff (Opposing agent defects; this agent cooperates)
     *
     *<p> 3. TEMPTATION to defect (Opposing agent cooperates; this agent defects)
     *
     *<p> 4. PUNISHMENT for mutual defection (Both agents defect)
     *
     *<p>The best strategy for both agents is to cooperate.  However, if an agent
     *chooses to defect when the other cooperates, the defecting agent will have
     *a greater advantage.  For a true game of PD, the energy scores for each
     *outcome should follow this rule: TEMPTATION > REWARD > PUNISHMENT > SUCKER
     *
     *<p>Here is an example of how much energy an agent could receive:
     *<br> REWARD     =>     5
     *<br> SUCKER     =>     2
     *<br> TEMPTATION =>     8
     *<br> PUNISHMENT =>     3
     *
     * @param adjacentAgent Agent playing PD with
     * @param othersID ID of the adjacent agent.
     * @see ComplexAgent#playPD(ComplexAgent)
     * @see <a href="http://en.wikipedia.org/wiki/Prisoner's_dilemma">Prisoner's Dilemma</a>
     */
    @SuppressWarnings("javadoc")
    public void playPDonStep(Agent me, BaseAgent adjacentAgent) {
        PDState meState = getAgentState(me);
        PDState otherState = getAgentState(adjacentAgent);

        playPD(me, meState, adjacentAgent);
        playPD(adjacentAgent, otherState, me);

        // Save result for future strategy (tit-for-tat, learning, etc.)
        meState.lastPDcheated = otherState.pdCheater;
        otherState.lastPDcheated = meState.pdCheater;

        /*
         * TODO LOW: The ability for the PD game to contend for the Get the food tiles immediately around each agents
         */

        if (!meState.pdCheater && !otherState.pdCheater) {
            /* Both cooperate */
            me.changeEnergy(+params.reward, new PDRewardCause());
            adjacentAgent.changeEnergy(+params.reward, new PDRewardCause());

        } else if (!meState.pdCheater && otherState.pdCheater) {
            /* Only other agent cheats */
            me.changeEnergy(+params.sucker, new PDSuckerCause());
            adjacentAgent.changeEnergy(+params.temptation, new PDTemptationCause());

        } else if (meState.pdCheater && !otherState.pdCheater) {
            /* Only this agent cheats */
            me.changeEnergy(+params.temptation, new PDTemptationCause());
            adjacentAgent.changeEnergy(+params.sucker, new PDSuckerCause());

        } else if (meState.pdCheater && otherState.pdCheater) {
            /* Both cheat */
            me.changeEnergy(+params.punishment, new PDPunishmentCause());
            adjacentAgent.changeEnergy(+params.punishment, new PDPunishmentCause());
        }

        if (otherState.pdCheater)
            iveBeenCheated(me, adjacentAgent);
    }

    /**
     * The agent will remember the last variable number of agents that
     * cheated it.  How many cheaters it remembers is determined by its
     * PD memory size.
     */
    private static void iveBeenCheated(Agent me, BaseAgent cheater) {
        me.rememberBadAgent(cheater);
        me.broadcast(new CheaterBroadcast(cheater, me), new BroadcastCheaterCause());
    }

    public static class PDCause implements Cause {
        @Override
        public String getName() { return "PD"; }
    }
    public static class PDRewardCause extends PDCause {
        @Override
        public String getName() { return "PD Reward"; }
    }
    public static class PDTemptationCause extends PDCause {
        @Override
        public String getName() { return "PD Temptation"; }
    }
    public static class PDSuckerCause extends PDCause {
        @Override
        public String getName() { return "PD Sucker"; }
    }
    public static class PDPunishmentCause extends PDCause {
        @Override
        public String getName() { return "PD Punishment"; }
    }

    public static class BroadcastCheaterCause extends BroadcastCause {
        @Override
        public String getName() { return "Broadcast Cheating"; }
    }

    @Override
    protected boolean validState(PDState value) {
        return value != null;
    }
}