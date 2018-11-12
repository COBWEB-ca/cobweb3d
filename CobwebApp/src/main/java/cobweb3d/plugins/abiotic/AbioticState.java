package cobweb3d.plugins.abiotic;

import cobweb3d.plugins.abiotic.factor.AbioticFactorState;
import cobweb3d.plugins.states.AgentState;
import cobwebutil.io.ConfList;
import cobwebutil.io.ConfXMLTag;

public class AbioticState implements AgentState {
    @ConfXMLTag("FactorStates")
    @ConfList(indexName = "Factor", startAtOne = true)
    public AbioticFactorState[] factorStates = new AbioticFactorState[0];

    @Deprecated // for reflection use only!
    public AbioticState() {
    }

    public AbioticState(AbioticAgentParams params) {
        this.factorStates = new AbioticFactorState[params.factorParams.length];
        for (int i = 0;i < params.factorParams.length; i++) {
            factorStates[i] = new AbioticFactorState(params.factorParams[i]);
        }
    }

    @Override
    protected AbioticState clone()  {
        try {
            AbioticState copy = (AbioticState) super.clone();
            copy.factorStates = new AbioticFactorState[this.factorStates.length];
            for (int i =0; i < factorStates.length; i++) {
                copy.factorStates[i] = this.factorStates[i].clone();
            }
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public boolean isTransient() {
        return false;
    }
    private static final long serialVersionUID = 1L;
}
