package cobweb3d.plugins.exchange;

import cobweb3d.plugins.exchange.utility.Calculation;
import cobweb3d.plugins.exchange.utility.UtilityFunctionParam;
import cobwebutil.io.CloneHelper;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

public class ExchangeAgentParams implements ParameterSerializable, Calculation {
    private static final long serialVersionUID = 12L;

    @ConfDisplayName("Initial x")
    @ConfXMLTag("initialX")
    public float initialX = 0;

    @ConfDisplayName("Initial y")
    @ConfXMLTag("initialY")
    public float initialY = 0;

    @ConfDisplayName("Utility function")
    @ConfXMLTag("utilityFunction")
    public UtilityFunctionParam utilityFunctionParam = new UtilityFunctionParam();

    //@Deprecated // for reflection use only!
    public ExchangeAgentParams() {
    }

    @Override
    public ExchangeAgentParams clone() {
        try {
            ExchangeAgentParams copy = (ExchangeAgentParams) super.clone();
            copy.utilityFunctionParam = this.utilityFunctionParam.clone();
            CloneHelper.resetMutatable(copy);
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public float calculateU(ExchangeState agentState) {
        if (agentState == null) return -1;
        return agentState.util = calculateU(agentState.x, agentState.y);
    }

    public float calculateU(float x, float y) {
        return calculateU(x, y, utilityFunctionParam.varA, utilityFunctionParam.varB);
    }

    @Override
    public float calculateU(float x, float y, float A, float B) {
        return utilityFunctionParam.formula.calculateU(x, y, A, B);
    }
}