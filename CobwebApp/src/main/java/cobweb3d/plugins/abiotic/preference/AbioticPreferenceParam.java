package cobweb3d.plugins.abiotic.preference;

import cobwebutil.io.CloneHelper;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

public class AbioticPreferenceParam implements ParameterSerializable {
    /**
     * Preferred abiotic factor value of the agent type.
     */
    @ConfXMLTag("PreferedValue")
    @ConfDisplayName("value")
    public float value;

    /**
     * Range of value from preferred that can be tolerated.
     */
    @ConfXMLTag("PreferedRange")
    @ConfDisplayName("value range")
    public float range;

    /**
     * How much of an effect deviation from the preferred value range will have.
     */
    @ConfXMLTag("DifferenceFactor")
    @ConfDisplayName("difference Factor")
    public float differenceFactor;


    /**
     * Calculates (dis)comfort value at given abiotic factor
     * Result is how far factorValue is out of the preferred range multiplied by differenceFactor
     *
     * @param factorValue abiotic factor value
     * @return comfort/discomfort for the value
     */
    public float score(float factorValue) {
        float diff = Math.abs(factorValue - value);
        diff = Math.max(diff - range, 0);
        float res = diff * differenceFactor;
        return res;
    }

    @Override
    public AbioticPreferenceParam clone() {
        try {
            AbioticPreferenceParam copy = (AbioticPreferenceParam) super.clone();
            CloneHelper.resetMutatable(copy);
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }
    private static final long serialVersionUID = 1L;
}
