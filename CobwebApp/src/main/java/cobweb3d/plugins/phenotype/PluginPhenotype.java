package cobweb3d.plugins.phenotype;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.states.AgentState;
import cobweb3d.ui.config.PropertyAccessor;

public class PluginPhenotype extends PropertyPhenotype {

    private static final long serialVersionUID = 2L;
    private Class<? extends AgentState> type;
    private PropertyAccessor stateParamAccessor;

    // TODO: Possible breaking change that would make name collisions less likely
    //@Override
    //public String getIdentifier() {
    //	return type.getSimpleName() + "." + super.getIdentifier();
    //}

    public PluginPhenotype(Class<? extends AgentState> type,
                           PropertyAccessor stateParamAccessor,
                           PropertyAccessor propertyAccessor) {
        super(propertyAccessor);
        this.type = type;
        this.stateParamAccessor = stateParamAccessor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PluginPhenotype) {
            PluginPhenotype o = (PluginPhenotype) obj;
            return o.type.equals(this.type) &&
                    o.stateParamAccessor.equals(this.stateParamAccessor) &&
                    super.equals(o);
        }
        return false;
    }

    @Override
    protected Object rootAccessor(BaseAgent a) {
        AgentState state = ((Agent) a).getState(type);
        if (state == null)
            return null;

        return stateParamAccessor.get(state);
    }
}
