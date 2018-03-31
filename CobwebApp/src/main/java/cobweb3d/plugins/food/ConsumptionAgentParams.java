package cobweb3d.plugins.food;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.ResizableParam;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfList;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

import java.util.Arrays;

public class ConsumptionAgentParams implements ParameterSerializable, ResizableParam {
    private static final long serialVersionUID = 12L;

    /**
     * Agent types this agent can transmit the disease to.
     */
    @ConfDisplayName("Can eat agent")
    @ConfXMLTag("canDiminish")
    @ConfList(indexName = "Agent", startAtOne = true)
    public boolean[] canEat = new boolean[0];

    /**
     * Agent types this agent can transmit the disease to.
     */
    @ConfDisplayName("Energy gain multiplier from agent ")
    @ConfXMLTag("energyMultiplier")
    @ConfList(indexName = "Agent", startAtOne = true)
    public float[] energyMultipler = new float[0];

    //@Deprecated // for reflection use only!
    public ConsumptionAgentParams() {
        for (int i = 0; i < energyMultipler.length; i++) energyMultipler[i] = 1f;
    }

    @Override
    public void resize(AgentFoodCountable size) {
        this.canEat = Arrays.copyOf(canEat, size.getAgentTypes());
        int prevSize = energyMultipler.length;
        this.energyMultipler = Arrays.copyOf(energyMultipler, size.getAgentTypes());
        for (int i = prevSize; i < size.getAgentTypes(); i++) {
            energyMultipler[i] = 1.0f;
        }
    }
}