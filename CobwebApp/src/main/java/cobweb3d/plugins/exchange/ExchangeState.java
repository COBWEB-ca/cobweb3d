package cobweb3d.plugins.exchange;

import cobweb3d.plugins.states.AgentState;
import cobwebutil.io.ConfXMLTag;

public class ExchangeState implements AgentState {

    @ConfXMLTag("x")
    public float x = 0;

    @ConfXMLTag("y")
    public float y = 0;

    @ConfXMLTag("u")
    public Float util = null;

    @Deprecated // for reflection use only!
    public ExchangeState() {
    }

    public ExchangeState(float x, float y, float util) {
        this.x = x;
        this.y = y;
        this.util = util;
    }

    public ExchangeState(ExchangeAgentParams agentParams) {
        this(agentParams.initialX, agentParams.initialY, agentParams.calculateU(agentParams.initialX, agentParams.initialY));
    }

    @Override
    public boolean isTransient() {
        return false;
    }
}
