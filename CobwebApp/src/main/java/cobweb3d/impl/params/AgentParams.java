package cobweb3d.impl.params;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.ResizableParam;
import cobwebutil.MutatableFloat;
import cobwebutil.MutatableInt;
import cobwebutil.io.CloneHelper;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;

public class AgentParams implements ResizableParam {
    private static final long serialVersionUID = -7852361484228627542L;

    /**
     * Initial number of agents.
     */
    @ConfDisplayName("Initial count")
    @ConfXMLTag("Agents")
    public int initialAgents = 1;

    /**
     * Initial number of agents.
     */
    @ConfDisplayName("Color")
    @ConfXMLTag("Color")
    public String color = "";

    /**
     * Initial energy amount.
     */
    @ConfDisplayName("Initial energy")
    @ConfXMLTag("InitEnergy")
    public MutatableInt initEnergy = new MutatableInt(100);

    /**
     * Energy used to step forward.
     */
    @ConfDisplayName("Step energy")
    @ConfXMLTag("StepEnergy")
    public MutatableInt stepEnergy = new MutatableInt(1);

    /**
     * Energy lost bumping into another agent.
     */
    @ConfDisplayName("Agent bump energy")
    @ConfXMLTag("StepAgentEnergy")
    public MutatableInt stepAgentEnergy = new MutatableInt(2);

    /**
     * Enable aging mode.
     */
    @ConfDisplayName("Aging")
    @ConfXMLTag("agingMode")
    public boolean agingMode = false;

    /**
     * Age limit after which the agent is forced to die.
     */
    @ConfDisplayName("Age limit")
    @ConfXMLTag("agingLimit")
    public MutatableInt agingLimit = new MutatableInt(300);

    /**
     * Enables message broadcasts.
     */
    @ConfDisplayName("Broadcast")
    @ConfXMLTag("broadcastMode")
    public boolean broadcastMode = false;

    /**
     * Makes broadcast radius depend on agent energy.
     * Formula is: radius = energy / 10 + 1.
     */
    @ConfDisplayName("Broadcast energy-based")
    @ConfXMLTag("broadcastEnergyBased")
    public boolean broadcastEnergyBased = false;

    /**
     * Radius of broadcast area.
     */
    @ConfDisplayName("Broadcast fixed range")
    @ConfXMLTag("broadcastFixedRange")
    public MutatableInt broadcastFixedRange = new MutatableInt(20);

    /**
     * Minimum agent energy to broadcast.
     */
    @ConfDisplayName("Broadcast minimum energy")
    @ConfXMLTag("broadcastEnergyMin")
    public MutatableInt broadcastEnergyMin = new MutatableInt(20);

    /**
     * Energy used up by broadcasting.
     */
    @ConfDisplayName("Broadcast cost")
    @ConfXMLTag("broadcastEnergyCost")
    public MutatableInt broadcastEnergyCost = new MutatableInt(5);

    @ConfDisplayName("Broadcast heard only by same type")
    @ConfXMLTag("broadcastSameTypeOnly")
    public boolean broadcastSameTypeOnly = false;


    public AgentParams() {

    }

    @Override
    public void resize(AgentFoodCountable envParams) {
        // Resize stuff arrays and stuff that needs to be resize. Ex. Foodweb array downstream.
    }

    @Override
    public BaseAgentParams clone() {
        try {
            BaseAgentParams copy = (BaseAgentParams) super.clone();
            CloneHelper.resetMutatable(copy);
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
