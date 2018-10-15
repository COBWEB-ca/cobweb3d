package cobweb3d.plugins.abiotic;

import cobweb3d.plugins.abiotic.factor.AgentFactorParams;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfList;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

import java.util.Arrays;

public class AbioticAgentParams implements ParameterSerializable {
    private static final long serialVersionUID = 14L;
    @ConfDisplayName("Factor")
    @ConfXMLTag("FactorParams")
    @ConfList(indexName = "Factor", startAtOne = true)
    public AgentFactorParams[] factorParams = new AgentFactorParams[0];

    public AbioticAgentParams() {
    }

    public void resizeFields(int fieldCount) {
        AgentFactorParams[] n = Arrays.copyOf(factorParams, fieldCount);
        for (int i = factorParams.length; i < n.length; i++) {
            n[i] = new AgentFactorParams();
        }
        factorParams = n;
    }
}
