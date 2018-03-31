package cobweb3d.plugins.ported.disease;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;

public class DiseaseParams extends PerAgentParams<DiseaseAgentParams> {

    private static final long serialVersionUID = 2L;
    private AgentFoodCountable size;

    public DiseaseParams(AgentFoodCountable size) {
        super(DiseaseAgentParams.class, size);
    }

    @Override
    protected DiseaseAgentParams newAgentParam() {
        return new DiseaseAgentParams(size);
    }

    @Override
    public void resize(AgentFoodCountable newSize) {
        this.size = newSize;
        super.resize(newSize);

        for (DiseaseAgentParams p : agentParams) {
            p.resize(this.size);
        }
    }
}
