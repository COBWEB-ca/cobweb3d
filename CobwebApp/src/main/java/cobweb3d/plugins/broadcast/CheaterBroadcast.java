package cobweb3d.plugins.broadcast;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.agent.Agent;

public class CheaterBroadcast extends BroadcastPacket {

    public final BaseAgent cheater;

    public CheaterBroadcast(BaseAgent cheater, Agent dispatcherId) {
        super(dispatcherId);
        this.cheater = cheater;
    }

    @Override
    public void process(Agent receiver) {
        receiver.rememberBadAgent(cheater);
    }

}
