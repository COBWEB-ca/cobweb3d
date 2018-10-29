package cobweb3d.plugins.abiotic.factor;

import cobweb3d.core.SimulationTimeSpace;
import cobwebutil.io.ParameterSerializable;

public abstract class AbioticFactor implements ParameterSerializable {
    public abstract float getValue(float x, float y, float z);

    public abstract float getMax();
    public abstract float getMin();

    public abstract String getName();

    /**
     * @param sim simulation time and space info
     */
    public void update(SimulationTimeSpace sim) {
        // nothing
    }

    public abstract AbioticFactor copy();

    private static final long serialVersionUID = 1L;
}
