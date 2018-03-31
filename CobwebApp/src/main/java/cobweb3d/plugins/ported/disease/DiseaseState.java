package cobweb3d.plugins.ported.disease;

import cobweb3d.plugins.states.AgentState;
import cobwebutil.io.ConfXMLTag;

public class DiseaseState implements AgentState {

    private static final long serialVersionUID = 1L;
    @ConfXMLTag("sick")
    public boolean sick = false;
    @ConfXMLTag("vaccinated")
    public boolean vaccinated = false;
    @ConfXMLTag("sickStart")
    public long sickStart = -1;
    @ConfXMLTag("vaccineEffectiveness")
    public float vaccineEffectiveness = 1;
    @ConfXMLTag("AgentParams")
    public DiseaseAgentParams agentParams;

    @Deprecated // for reflection use only!
    public DiseaseState() {
    }

    public DiseaseState(DiseaseAgentParams agentParams, boolean sick, boolean vaccinated, long sickStart) {
        this.agentParams = agentParams;
        this.sick = sick;
        this.vaccinated = vaccinated;
        this.sickStart = sickStart;
    }

    public DiseaseState(DiseaseAgentParams agentParams, boolean sick, boolean vaccinated, float vaccineEffectiveness) {
        this.agentParams = agentParams;
        this.sick = sick;
        this.vaccinated = vaccinated;
        this.vaccineEffectiveness = vaccineEffectiveness;
    }

    @Override
    public boolean isTransient() {
        return false;
    }
}