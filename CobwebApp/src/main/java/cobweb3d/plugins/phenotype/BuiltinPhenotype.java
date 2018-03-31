package cobweb3d.plugins.phenotype;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.agent.Agent;
import cobweb3d.ui.config.PropertyAccessor;

/**
 * Phenotype that uses Reflection to modify fields of ComplexAgentParams
 */
public class BuiltinPhenotype extends PropertyPhenotype {

    private static final long serialVersionUID = 2L;

    /**
     * @param x field to modify
     */
    BuiltinPhenotype(PropertyAccessor x) {
        super(x);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BuiltinPhenotype) {
            BuiltinPhenotype o = (BuiltinPhenotype) obj;
            return super.equals(o);
        }
        return false;
    }

    @Override
    protected Object rootAccessor(BaseAgent a) {
        return ((Agent) a).params;
    }
}
