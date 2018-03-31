package cobweb3d.plugins.transform;

import cobweb3d.plugins.states.AgentState;
import cobwebutil.io.ConfList;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

import java.util.LinkedList;
import java.util.List;

public class TransformationState implements AgentState {

    @ConfXMLTag("previousTypes")
    @ConfList(indexName = "PrevTypes", startAtOne = false)
    List<TransformationEvent> transformations = new LinkedList<>();

    @Deprecated // for reflection use only!
    public TransformationState() {
    }

    public TransformationState(TransformationState prevState, int curType, int tick) {
        if (prevState != null) transformations.addAll(prevState.transformations);
        transformations.add(new TransformationEvent(curType, tick));
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    private static class TransformationEvent implements ParameterSerializable {
        int lastType;
        //int tick;

        public TransformationEvent(int lastType, int tick) {
            this.lastType = lastType;
            //this.tick = tick;
        }
    }
}
