package cobweb3d.impl.params;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;

public class BaseAgentParams extends PerAgentParams<AgentParams> {

    private static final long serialVersionUID = 2L;
    private AgentFoodCountable size;

    public BaseAgentParams(AgentFoodCountable size) {
        super(AgentParams.class);
        this.size = size;
        resize(size);
    }

    @Override
    protected AgentParams newAgentParam() {
        return new AgentParams();
    }

    @Override
    public void resize(AgentFoodCountable envParams) {
        size = envParams;
        super.resize(size);

        for (AgentParams complexAgentParams : agentParams) {
            complexAgentParams.resize(size);
        }
    }
}
