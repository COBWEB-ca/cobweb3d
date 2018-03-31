package cobweb3d.plugins.food;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;
import cobweb3d.core.params.ResizableParam;

public class ConsumptionParams extends PerAgentParams<ConsumptionAgentParams> {

    private static final long serialVersionUID = 2L;
    private AgentFoodCountable size;

    public ConsumptionParams(AgentFoodCountable size) {
        super(ConsumptionAgentParams.class, size);
    }

    @Override
    protected ConsumptionAgentParams newAgentParam() {
        return new ConsumptionAgentParams();
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