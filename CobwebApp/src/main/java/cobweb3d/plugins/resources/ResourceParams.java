package cobweb3d.plugins.resources;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;

public class ResourceParams extends PerAgentParams<ResourceAgentParams> {

    private static final long serialVersionUID = 2L;
    private AgentFoodCountable size;

    public ResourceParams(AgentFoodCountable size) {
        super(ResourceAgentParams.class, size);
    }

    @Override
    protected ResourceAgentParams newAgentParam() {
        return new ResourceAgentParams();
    }

    @Override
    public void resize(AgentFoodCountable newSize) {
        this.size = newSize;
        super.resize(newSize);
    }
}
