package cobweb3d.plugins.personality;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;

public class PersonalityParams extends PerAgentParams<PersonalityAgentParams> {
    @ConfDisplayName("Personality enabled")
    @ConfXMLTag("enable")
    public boolean personalitiesEnabled = true;

    // PD will be enabled so long as personalities are enabled

    @ConfDisplayName("Temptation (PD comes with personalities, KEEP PD OFF IN OTHER TAB)")
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



    public PersonalityParams(AgentFoodCountable size) {
        super(PersonalityAgentParams.class, size);
    }

    @Override
    protected PersonalityAgentParams newAgentParam() {
        return new PersonalityAgentParams();
    }

    private static final long serialVersionUID = 2L;
}
