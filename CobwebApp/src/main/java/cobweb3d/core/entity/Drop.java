package cobweb3d.core.entity;

import cobweb3d.core.Updatable;
import cobweb3d.core.agent.BaseAgent;

/**
 * Contains methods
 */
public interface Drop extends Updatable {

    boolean canStep(BaseAgent agent);

    void onStep(BaseAgent agent);

    void prepareRemove();
}
