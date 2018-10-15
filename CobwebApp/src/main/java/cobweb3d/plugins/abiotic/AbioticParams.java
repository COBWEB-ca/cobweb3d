package cobweb3d.plugins.abiotic;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;
import cobweb3d.plugins.abiotic.factor.AbioticFactor;
import cobwebutil.io.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbioticParams extends PerAgentParams<AbioticAgentParams> {

    @ConfSaveInstanceClass
    @ConfDisplayName("Factor")
    @ConfXMLTag("Factors")
    @ConfList(indexName = "Factor", startAtOne = true)
    @ConfListType(AbioticFactor.class)
    public List<AbioticFactor> factors = new ArrayList<>();


    public AbioticParams(AgentFoodCountable size) {
        super(AbioticAgentParams.class);
        resize(size);
    }

    @Override
    protected AbioticAgentParams newAgentParam() {
        return new AbioticAgentParams();
    }

    @Override
    public void resize(AgentFoodCountable envParams) {
        super.resize(envParams);

        for (int i = 0; i < agentParams.length; i++) {
            agentParams[i].resizeFields(factors.size());
        }
    }


    static final String STATE_NAME_ABIOTIC_PENALTY = "Abiotic %d Penalty";


    public Collection<String> getStatePluginKeys() {
        Collection<String> result = new ArrayList<>();
        for (int i = 0; i < factors.size(); i++) {
            result.add(String.format(STATE_NAME_ABIOTIC_PENALTY, i + 1));
        }
        return result;
    }

    private static final long serialVersionUID = 2L;
}
