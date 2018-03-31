package cobweb3d.core.params.phenotype;

import cobweb3d.core.agent.BaseAgent;

/**
 * Phenotype that does nothing
 */
public class NullPhenotype extends Phenotype {

    private static final long serialVersionUID = 2L;

    @Override
    public void modifyValue(Object cause, BaseAgent a, float m) {
        // Nothing
    }

    @Override
    public void unmodifyValue(Object cause, BaseAgent a) {
        // Nothing
    }

    @Override
    public String getIdentifier() {
        return "None";
    }

    @Override
    public String getName() {
        return "[Null]";
    }
}
