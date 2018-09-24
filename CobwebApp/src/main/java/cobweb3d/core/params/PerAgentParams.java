package cobweb3d.core.params;

import cobweb3d.core.agent.BaseAgent;
import cobwebutil.io.ConfList;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

import java.lang.reflect.Array;
import java.util.Arrays;


public abstract class PerAgentParams<T extends ParameterSerializable> implements PerTypeParam<T> {

    private static final long serialVersionUID = 1L;

    @ConfXMLTag("AgentParams")
    @ConfList(indexName = "Agent", startAtOne = true)
    public T[] agentParams;
    private Class<T> agentParamClass;

    /**
     * Creates agent param array of size 0. Call resize() to set required size after.
     */
    @SuppressWarnings("unchecked")
    public PerAgentParams(Class<T> agentParamClass) {
        this.agentParamClass = agentParamClass;
        agentParams = (T[]) Array.newInstance(this.agentParamClass, 0);
    }

    /**
     * Create agent param array and resize to initial size.
     * Requires that newAgentParam() does not depend on any new members of the subclass!
     */
    public PerAgentParams(Class<T> agentparClass, AgentFoodCountable initialSize) {
        this(agentparClass);
        resize(initialSize);
    }

    @Override
    public void resize(AgentFoodCountable envParams) {
        T[] n = Arrays.copyOf(agentParams, envParams.getAgentTypes());

        for (int i = agentParams.length; i < envParams.getAgentTypes(); i++) {
            n[i] = newAgentParam();
        }
        agentParams = n;
    }

    protected abstract T newAgentParam();

    @Override
    public T[] getPerTypeParams() {
        return agentParams;
    }

    public T getAgentParams(BaseAgent agent) {
        int type = agent.getType();
        if (type >= 0 && type < agentParams.length) return agentParams[type];
        else return null;
    }

    /**
     * Wrapper for getAgentParams(BaseAgent agent).
     *
     * @param agent The agent whose params we query.
     * @return Parameters of the agent.
     */
    public T of(BaseAgent agent) {
        return getAgentParams(agent);
    }
}
