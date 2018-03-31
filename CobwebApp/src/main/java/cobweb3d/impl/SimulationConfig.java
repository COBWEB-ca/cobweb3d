package cobweb3d.impl;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.BaseEnvironmentParams;
import cobweb3d.core.params.ControllerParams;
import cobweb3d.core.params.ResizableParam;
import cobweb3d.impl.ai.SimpleController;
import cobweb3d.impl.ai.SimpleControllerParams;
import cobweb3d.impl.logging.LogConfig;
import cobweb3d.impl.params.BaseAgentParams;
import cobweb3d.plugins.MutatorListenerConfig;
import cobweb3d.plugins.diminish.DiminishParams;
import cobweb3d.plugins.exchange.ExchangeParams;
import cobweb3d.plugins.food.ConsumptionParams;
import cobweb3d.plugins.ported.disease.DiseaseParams;
import cobweb3d.plugins.reproduction.ReproductionParams;
import cobweb3d.plugins.transform.TransformationParams;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfSaveInstanceClass;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to organize, modify, and access simulation parameters.
 */
public class SimulationConfig implements ParameterSerializable, AgentFoodCountable, SimulationParams {

    private static final long serialVersionUID = 2L;
    public String fileName = "default simulation";
    /**
     * Random number generator seed for repeating the simulation exactly.
     */
    @ConfDisplayName("Random seed")
    @ConfXMLTag("randomSeed")
    public long randomSeed = 42;
    @ConfXMLTag("Environment")
    public BaseEnvironmentParams envParams = new BaseEnvironmentParams();
    @ConfXMLTag("Agents")
    public BaseAgentParams agentParams = new BaseAgentParams(this);
    private int agentTypeCount = 4;
    @ConfSaveInstanceClass
    @ConfXMLTag("ControllerConfig")
    public ControllerParams controllerParams = new SimpleControllerParams(this);

    /**
     * Spawns new agents.
     */
    @ConfDisplayName("Spawn new agents")
    @ConfXMLTag("spawnNewAgents")
    public boolean spawnNewAgents = true;
    /**
     * Keeps existing agents.
     */
    @ConfDisplayName("Keep old agents")
    @ConfXMLTag("keepOldAgents")
    public boolean keepOldAgents = false;

    /**
     * Creates the default Cobweb simulation parameters.
     */
    public SimulationConfig() {
    }

    public int getAgentTypes() {
        return agentTypeCount;
    }

    /**
     * Number of BaseAgent types.
     */
    @ConfDisplayName("Agent types")
    @ConfXMLTag("AgentTypeCount")
    public void setAgentTypes(int count) {
        this.agentTypeCount = count;
        agentCountChanged();
    }

    @ConfXMLTag("LogConfig")
    public LogConfig logConfig = new LogConfig();
    @ConfXMLTag("MutatorConfig")
    public MutatorListenerConfig mutatorConfig = new MutatorListenerConfig();

    @ConfXMLTag("Reproduction")
    public ReproductionParams reproductionParams = new ReproductionParams(this);
    @ConfXMLTag("Consumption")
    public ConsumptionParams consumptionParams = new ConsumptionParams(this);
    @ConfXMLTag("Diminish")
    public DiminishParams diminishParams = new DiminishParams(this);
    @ConfXMLTag("Exchange")
    public ExchangeParams exchangeParams = new ExchangeParams(this);
    @ConfXMLTag("Transformation")
    public TransformationParams transformationParams = new TransformationParams(this);
    @ConfXMLTag("Disease")
    public DiseaseParams diseaseParams = new DiseaseParams(this);



    private String controllerName = SimpleController.class.getName();

    private void agentCountChanged() {
        for (Field f : this.getClass().getFields()) {
            if (ResizableParam.class.isAssignableFrom(f.getType())) {
                ResizableParam param;
                try {
                    param = (ResizableParam) f.get(this);
                    param.resize(this);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException("Something broke in agentCountChanged()", ex);
                }
            }
        }
    }

    public String getControllerName() {
        return controllerName;
    }

    public boolean isContinuation() {
        return keepOldAgents;
    }

    public <T> T getParam(Class<T> pt) {
        if (pt.isAssignableFrom(this.getClass())) {
            @SuppressWarnings("unchecked")
            T result = (T) this;
            return result;
        } else {
            for (Field f : this.getClass().getFields()) {
                if (pt.isAssignableFrom(f.getType())) {
                    try {
                        @SuppressWarnings("unchecked")
                        T result = (T) f.get(this);
                        return result;
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException("Could not get parameter " + f.getName(), ex);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Class name of the controller object.
     */
    @ConfDisplayName("Controller type")
    @ConfXMLTag("ControllerName")
    public void setControllerName(String name) {
        controllerName = name;
        try {
            controllerParams = (ControllerParams) Class.forName(controllerName + "Params")
                    .getConstructor(SimulationParams.class)
                    .newInstance((SimulationParams) this);
        } catch (Exception ex) {
            try {
                controllerParams = (ControllerParams) Class.forName(controllerName + ".Params")
                        .getConstructor(SimulationParams.class)
                        .newInstance((SimulationParams) this);
            } catch (Exception x) {
                throw new RuntimeException("Could not set up controller", ex);
            }
        }
    }

    @Override
    public List<String> getPluginParameters() {
        List<String> result = new ArrayList<String>();
        // TODO: Add AI plugin state keys.
        return result;
    }
}
