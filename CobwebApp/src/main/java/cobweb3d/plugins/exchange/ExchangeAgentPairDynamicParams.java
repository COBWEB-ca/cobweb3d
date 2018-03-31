package cobweb3d.plugins.exchange;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.ResizableParam;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

public class ExchangeAgentPairDynamicParams implements ParameterSerializable, ResizableParam {
    private static final long serialVersionUID = 12L;

    @ConfDisplayName("Dynamic Quantities Enabled")
    @ConfXMLTag("enabled")
    public boolean enabled = false;

    @ConfDisplayName("Lower bound")
    @ConfXMLTag("lowerBound")
    public float lowerBound = 0;

    @ConfDisplayName("Upper bound")
    @ConfXMLTag("upperBound")
    public float upperBound = 1;

    @ConfDisplayName("Increment")
    @ConfXMLTag("increment")
    public float increment = 1;

    //@Deprecated // for reflection use only!
    public ExchangeAgentPairDynamicParams() {
    }

    @Override
    public void resize(AgentFoodCountable size) {
        // TODO:
    }
}
