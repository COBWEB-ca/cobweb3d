package cobweb3d.plugins.vision;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;

public class VisionParams extends PerAgentParams<VisionAgentParams> {
    private static final long serialVersionUID = 2L;
    private AgentFoodCountable size;

    public VisionParams(AgentFoodCountable size) {
        super(VisionAgentParams.class, size);
    }

    @Override
    protected VisionAgentParams newAgentParam() {
        return new VisionAgentParams();
    }

    @Override
    public void resize(AgentFoodCountable newSize) {
        this.size = newSize;
        super.resize(newSize);
    }
}
