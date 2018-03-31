package cobweb3d.plugins.diminish;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.ResizableParam;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfList;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

import java.util.Arrays;

public class DiminishAgentParams implements ParameterSerializable, ResizableParam {
    private static final long serialVersionUID = 12L;

    /**
     * Agent types this agent can transmit the disease to.
     */
    @ConfDisplayName("Can Diminish Agent")
    @ConfXMLTag("canDiminish")
    @ConfList(indexName = "Agent", startAtOne = true)
    public boolean[] canDiminish = new boolean[0];

    /**
     * Agent types this agent can transmit the disease to.
     */
    @ConfDisplayName("Amount of Energy To Diminish")
    @ConfXMLTag("energyDiminishedAbs")
    @ConfList(indexName = "Agent", startAtOne = true)
    public int[] energyDiminishedAbs = new int[0];

    //@Deprecated // for reflection use only!
    public DiminishAgentParams() {
        for (int i = 0; i < energyDiminishedAbs.length; i++) energyDiminishedAbs[i] = 0;
    }

    @Override
    public void resize(AgentFoodCountable size) {
        this.canDiminish = Arrays.copyOf(canDiminish, size.getAgentTypes());
        int prevSize = energyDiminishedAbs.length;
        this.energyDiminishedAbs = Arrays.copyOf(energyDiminishedAbs, size.getAgentTypes());
        for (int i = prevSize; i < size.getAgentTypes(); i++) {
            energyDiminishedAbs[i] = 0;
        }
    }
}