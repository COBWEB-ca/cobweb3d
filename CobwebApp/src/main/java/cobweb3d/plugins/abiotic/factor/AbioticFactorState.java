package cobweb3d.plugins.abiotic.factor;

import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

public class AbioticFactorState implements ParameterSerializable {
    @ConfXMLTag("AgentParams")
    public AgentFactorParams agentParams;

    @Deprecated // for reflection use only!
    public AbioticFactorState(){
    }

    public AbioticFactorState(AgentFactorParams agentParams) {
        this.agentParams = agentParams;
    }

    @Override
    public AbioticFactorState clone() {
        try {
            AbioticFactorState copy = (AbioticFactorState) super.clone();
            copy.agentParams = this.agentParams.clone();
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static final long serialVersionUID = 1L;
}
