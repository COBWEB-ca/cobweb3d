package cobweb3d.plugins.pd;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;

public class PDParams extends PerAgentParams<PDAgentParams> {
    /**
     * Agents can play prisoner's dilemma game when they meet.
     */
    @ConfDisplayName("PD Enabled")
    @ConfXMLTag("enable")
    public boolean enable = true;

    /**
     * Temptation to defect.
     */
    @ConfDisplayName("Temptation")
    @ConfXMLTag("temptation")
    public int temptation = 20;

    /**
     * Reward for mutual cooperation.
     */
    @ConfDisplayName("Reward")
    @ConfXMLTag("reward")
    public int reward = 10;

    /**
     * Punishment for mutual defection.
     */
    @ConfDisplayName("Punishment")
    @ConfXMLTag("punishment")
    public int punishment = 0;

    /**
     * Sucker's payoff
     */
    @ConfDisplayName("Sucker's payoff")
    @ConfXMLTag("sucker")
    public int sucker = -5;



    public PDParams(AgentFoodCountable size) {
        super(PDAgentParams.class, size);
    }

    @Override
    protected PDAgentParams newAgentParam() {
        return new PDAgentParams();
    }

    private static final long serialVersionUID = 2L;
}