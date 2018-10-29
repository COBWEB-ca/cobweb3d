package cobweb3d.plugins.abiotic.factor;

import cobweb3d.core.params.phenotype.NullPhenotype;
import cobweb3d.core.params.phenotype.Phenotype;
import cobweb3d.plugins.abiotic.preference.AbioticPreferenceParam;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfSquishParent;
import cobwebutil.io.ConfXMLTag;

public class AgentFactorParams {
    @ConfDisplayName("Preference")
    @ConfSquishParent
    public AbioticPreferenceParam preference = new AbioticPreferenceParam();

    @ConfDisplayName("Parameter")
    @ConfXMLTag("Parameter")
    public Phenotype parameter = new NullPhenotype();

    @Override
    public AgentFactorParams clone() {
        try {
            AgentFactorParams copy = (AgentFactorParams) super.clone();
            copy.preference = this.preference.clone();
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static final long serialVersionUID = 1L;
}
