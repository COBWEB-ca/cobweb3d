package cobweb3d.plugins.diminish;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;
import cobweb3d.core.params.ResizableParam;

public class DiminishParams extends PerAgentParams<DiminishAgentParams> {

    private static final long serialVersionUID = 2L;
    private AgentFoodCountable size;

    public DiminishParams(AgentFoodCountable size) {
        super(DiminishAgentParams.class, size);
    }

    @Override
    protected DiminishAgentParams newAgentParam() {
        return new DiminishAgentParams();
    }

    @Override
    public void resize(AgentFoodCountable newSize) {
        this.size = newSize;
        for (ResizableParam resizableParam : getPerTypeParams()) {
            resizableParam.resize(newSize);
        }
        super.resize(newSize);
    }
}