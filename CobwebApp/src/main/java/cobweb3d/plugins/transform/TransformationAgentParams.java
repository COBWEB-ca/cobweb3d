package cobweb3d.plugins.transform;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.ResizableParam;
import cobwebutil.MutatableInt;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfList;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

import java.util.Arrays;

public class TransformationAgentParams implements ParameterSerializable, ResizableParam {
    private static final long serialVersionUID = 12L;

    @ConfDisplayName("Exchange: ")
    @ConfXMLTag("exchangeTransformationAgentParams")
    public ExchangeTransformationAgentParams exchangeTransformationAgentParams = new ExchangeTransformationAgentParams();

    @ConfDisplayName("Consumption: ")
    @ConfXMLTag("consumeTransformationAgentParams")
    public ConsumptionTransformationAgentParams consumptionTransformationAgentParams = new ConsumptionTransformationAgentParams();

    //@Deprecated // for reflection use only!
    public TransformationAgentParams() {
    }

    @Override
    public void resize(AgentFoodCountable size) {
        consumptionTransformationAgentParams.resize(size);
    }

    public static class ExchangeTransformationAgentParams implements ParameterSerializable {
        private static final long serialVersionUID = 12L;

        @ConfDisplayName("Transformation Enabled")
        @ConfXMLTag("enabled")
        public boolean enabled = false;

        /**
         * Chance that bumping into another agent will result in sexual breeding.
         */
        @ConfDisplayName("Destination Type")
        @ConfXMLTag("transformTo")
        public int destType = 1;

        /**
         * Chance that bumping into another agent will result in sexual breeding.
         */
        @ConfDisplayName("x Threshold")
        @ConfXMLTag("transformationXThreshold")
        public MutatableInt transformationX = new MutatableInt(100);

        //@Deprecated // for reflection use only!
        public ExchangeTransformationAgentParams() {
        }
    }

    public static class ConsumptionTransformationAgentParams implements ParameterSerializable, ResizableParam {
        private static final long serialVersionUID = 12L;

        /**
         * Agent types this agent can transmit the disease to.
         */
        @ConfDisplayName("Transformation Enabled on Consume ")
        @ConfXMLTag("enabled")
        @ConfList(indexName = "Agent", startAtOne = true)
        public boolean[] canTransform = new boolean[0];

        /**
         * Agent types this agent can transmit the disease to.
         */
        @ConfDisplayName("Destination Type on Consume ")
        @ConfXMLTag("transformTo")
        @ConfList(indexName = "Agent", startAtOne = true)
        public int[] destType = new int[0];

        //@Deprecated // for reflection use only!
        public ConsumptionTransformationAgentParams() {
            for (int i = 0; i < destType.length; i++) destType[i] = 1;
        }

        @Override
        public void resize(AgentFoodCountable size) {
            this.canTransform = Arrays.copyOf(canTransform, size.getAgentTypes());
            int prevSize = destType.length;
            this.destType = Arrays.copyOf(destType, size.getAgentTypes());
            for (int i = prevSize; i < size.getAgentTypes(); i++) {
                destType[i] = 1;
            }
        }
    }
}