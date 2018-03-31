package cobweb3d.plugins.reproduction;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;

public class ReproductionParams extends PerAgentParams<ReproductionAgentParams> {

    private static final long serialVersionUID = 2L;
    private AgentFoodCountable size;

    public ReproductionParams(AgentFoodCountable size) {
        super(ReproductionAgentParams.class, size);
    }

    @Override
    protected ReproductionAgentParams newAgentParam() {
        return new ReproductionAgentParams();
    }

    @Override
    public void resize(AgentFoodCountable newSize) {
        this.size = newSize;
        super.resize(newSize);
    }
}
