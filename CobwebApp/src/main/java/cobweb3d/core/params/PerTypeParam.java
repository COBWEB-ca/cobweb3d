package cobweb3d.core.params;

import cobwebutil.io.ParameterSerializable;

/**
 * Parameter that depends on the number of agent types in the simulation
 */
public interface PerTypeParam<T extends ParameterSerializable> extends ResizableParam {
    T[] getPerTypeParams();
}
