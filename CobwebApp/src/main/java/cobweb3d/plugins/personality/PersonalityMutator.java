package cobweb3d.plugins.personality;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.entity.Cause;
import cobweb3d.core.environment.Topology;
import cobweb3d.core.location.Direction;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.impl.Simulation;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.broadcast.CheaterBroadcast;
import cobweb3d.plugins.mutators.ContactMutator;
import cobweb3d.plugins.mutators.MoveMutator;
import cobweb3d.plugins.mutators.StatefulSpawnMutatorBase;

/*
 * So the mutator only controls the PD portion.
 * The swarm-esque part will be just an override of the controller's choice of input. But a method here
 * will be used to determine the location of the closest few agents and move in that direction.
 */
public class PersonalityMutator extends StatefulSpawnMutatorBase<PersonalityState, PersonalityParams> implements ContactMutator, MoveMutator {
    SimulationInternals sim;
    PersonalityParams params;

    // Return whether something was actually overridden or not
    @Override
    public boolean overrideMove(BaseAgent ag) {

        Simulation simulation = (Simulation) sim;
        Agent agent = (Agent) ag;

        PersonalityState state = agent.getState(PersonalityState.class);
        if (state == null) {
            return false;
        }
        // Shouldn't use this if the agents don't have personalities or their openness or neuroticism
        // isn't high enough to warrant any special moves

        /*
        if (!state.agentParams.personalitiesEnabled ||
                (state.agentParams.openness < 0.25 && state.agentParams.neuroticism < 0.25)) {
            return false;
        }
        */

        if (!state.agentParams.personalitiesEnabled ||
                (simulation.getRandom().nextFloat() > state.agentParams.openness &&
                        simulation.getRandom().nextFloat() > state.agentParams.neuroticism)) {
            return false;
        }

        // Now find the closest agent
        Agent closest = simulation.environment.getClosestAgent(agent);
        LocationDirection l2 = closest.getPosition();
        LocationDirection l1 = agent.getPosition();
        if (simulation.getTopology().getDistance(l1, l2) < 1.5) {
            agent.step();
        } else {
            Direction d = l1.direction;
            int x = l2.x; int y = l2.y; int z = l2.z;
            if (x > l1.x) {
                if (d.equals(Direction.xPos)) {
                    //System.out.println(1);
                    agent.step();
                } else {
                    //System.out.println(2);
                    agent.turnAbsoluteRight();
                }
            } else if (x < l1.x) {
                if (d.equals(Direction.xNeg)) {
                    //System.out.println(3);
                    agent.step();
                } else {
                    //System.out.println(4);
                    agent.turnAbsoluteLeft();
                }
            } else { // now they are on the same x axis
                if (y > l1.y) {
                    if (d.equals(Direction.yNeg)) {
                        //System.out.println(5);
                        agent.step();
                    } else {
                        //System.out.println(6);
                        agent.turnAbsoluteUp();
                    }
                } else if (y < l1.y) {
                    if (d.equals(Direction.yPos)) {
                        //System.out.println(7);
                        agent.step();
                    } else {
                        //System.out.println(8);
                        agent.turnAbsoluteDown();
                    }
                } else { // now they are on the same y axis and x axis
                    if (z > l1.z) {
                        if (d.equals(Direction.zPos)) {
                            //System.out.println(9);
                            agent.step();
                        } else {
                            //System.out.println(10);
                            agent.turnAbsoluteIn();
                        }
                    } else if (z < l1.z) {
                        if (d.equals(Direction.zNeg)) {
                            //System.out.println(11);
                            agent.step();
                        } else {
                            //System.out.println(12);
                            agent.turnAbsoluteOut();
                        }
                    } else {
                        System.out.println("Warning in personality mutator override move");
                        // shouldn't happen
                    }
                }
            }
        }
        return true;
    }

    public PersonalityMutator(SimulationInternals sim) {
        super(PersonalityState.class, sim);
        this.sim = sim;
    }


    @Override
    public PersonalityState stateForNewAgent(BaseAgent agent) {
        if (!params.personalitiesEnabled) {
            return null;
        }
        return new PersonalityState(params.agentParams[agent.getType()].clone());
    }

    @Override
    protected PersonalityState stateFromParent(BaseAgent agent, PersonalityState parentState) {
        if (!params.personalitiesEnabled) {
            return null;
        }

        return new PersonalityState(parentState.agentParams.clone());
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

    // Need to redefine PD in terms of the personality traits
    // Use return value to determine whether the opposite PD needs to be played or not.

    public boolean playPD(PersonalityState meState, PersonalityState otherState) {

        if (sim.getRandom().nextFloat() < meState.agentParams.extroversion ||
                sim.getRandom().nextFloat() < otherState.agentParams.extroversion) {
            return false;
        }

        // If this agent is agreeable and the other is conscientious, then <me> will lose and <other> will win
        if (meState.agentParams.agreeableness + otherState.agentParams.consciousness > 1.5) {
            meState.pdCheater = false;
            otherState.pdCheater = true;
            return false;
        }
        // If this agent is conscientious and the other is agreeable, then <me> will win and <other> will lose
        if (meState.agentParams.consciousness + otherState.agentParams.agreeableness > 1.5) {
            meState.pdCheater = true;
            otherState.pdCheater = false;
            return false;
        }

        // Otherwise we are going to play normal PD
        // Calculate cooperation probability based on how close they are to agreeable/conscientious
        double coopProb = meState.agentParams.consciousness - meState.agentParams.agreeableness + 0.5;

        if (sim.getRandom().nextFloat() >  coopProb) {
            meState.pdCheater = true;
        } else {
            meState.pdCheater = false;
        }
        return true; // Because other needs to play PD
    }

    public void playPDonStep(Agent me, BaseAgent adjacentAgent) {
        PersonalityState meState = getAgentState(me);
        PersonalityState otherState = getAgentState(adjacentAgent);

        // Calculate the possibility that the two agents, given their personalities, are going to play
        // prisoner's dilemma.
        float probOfPlayingMe = (float) Math.sqrt(Math.max(meState.agentParams.agreeableness, meState.agentParams.consciousness) * (1 - meState.agentParams.openness));
        float probOfPlayingOther = (float) Math.sqrt(Math.max(otherState.agentParams.agreeableness, otherState.agentParams.consciousness)* (1 - otherState.agentParams.openness));
        if (sim.getRandom().nextFloat() > probOfPlayingMe || sim.getRandom().nextFloat() > probOfPlayingOther) {
            return;
        }

        if (playPD(meState, otherState)) {
            playPD(otherState, meState);
        }

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

    private static void iveBeenCheated(Agent me, BaseAgent cheater) {
        me.rememberBadAgent(cheater);
        me.broadcast(new CheaterBroadcast(cheater, me), new BroadcastCheaterCause());
    }


    @Override
    protected boolean validState(PersonalityState state) {
        return state != null;
    }

    @Override
    public void setParams(SimulationTimeSpace sim, PersonalityParams params, int agentTypes) {
        this.sim = (SimulationInternals) sim;
        this.params = params;
    }

    @Override
    public boolean acceptsParam(Class<?> object) {
        return object.isAssignableFrom(PersonalityParams.class);
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

    public static class BroadcastCheaterCause extends Agent.BroadcastCause {
        @Override
        public String getName() { return "Broadcast Cheating"; }
    }
}
