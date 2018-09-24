package cobweb3d.plugins.resources;

import cobweb3d.core.params.PerAgentParams;

public class ResourceParams extends PerAgentParams<ResourceAgentParams> {

    private static final long serialVersionUID = 2L;

    public ResourceParams()
    @Override
    protected ResourceAgentParams newAgentParam() {
        return new ResourceAgentParams();
    }

}
