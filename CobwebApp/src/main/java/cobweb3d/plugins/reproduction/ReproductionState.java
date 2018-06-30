package cobweb3d.plugins.reproduction;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.plugins.states.AgentState;
import cobwebutil.io.ConfXMLTag;

public class ReproductionState implements AgentState {
    // pregnancyPeriod is set value while pregPeriod constantly changes
    @ConfXMLTag("pregPeriod")
    protected int pregPeriod = Integer.MAX_VALUE;

    @ConfXMLTag("pregnant")
    protected boolean pregnant = false;

    // Husband of the mother agent. If the agent breeds asexually, this field will be null.
    @ConfXMLTag("breedPartner")
    protected BaseAgent breedPartner;

    @Deprecated // for reflection use only!
    public ReproductionState() {
    }

    private ReproductionState(int pregPeriod, boolean pregnant, BaseAgent breedPartner) {
        this.pregPeriod = pregPeriod;
        this.breedPartner = breedPartner;
        this.pregnant = pregnant;
    }

    /**
     * A static method which returns an instance of this class.
     *
     * @param pregPeriod Time required for the baby to born.
     * @param breedPartner Husband of the mother agent. If the agent breeds asexually, this field will be null.
     * @return A reproduction state with the given pregPeriod and the breedPartner.
     */
    public static ReproductionState makePregnantState(int pregPeriod, BaseAgent breedPartner) {
        return new ReproductionState(pregPeriod, true, breedPartner);
    }

    @Override
    public boolean isTransient() {
        return false;
    }
}
