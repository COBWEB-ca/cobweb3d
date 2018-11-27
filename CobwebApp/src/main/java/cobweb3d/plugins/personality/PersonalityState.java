package cobweb3d.plugins.personality;

import cobweb3d.plugins.states.AgentState;
import cobwebutil.io.ConfXMLTag;

public class PersonalityState implements AgentState {
    @ConfXMLTag("pdCheater")
    public boolean pdCheater;

    @ConfXMLTag("AgentParams")
    public PersonalityAgentParams agentParams;

    @Deprecated
    public PersonalityState() {
    }

    public PersonalityState(PersonalityAgentParams personalityAgentParams) {
        this.agentParams = personalityAgentParams;
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    private static final long serialVersionUID = 1L;

}
