package cobweb3d.ui.swing.config.pages;

import cobweb3d.impl.params.AgentParams;
import cobweb3d.ui.swing.config.TableConfigPage;
import cobweb3d.ui.util.TypeColorEnumeration;

public class AgentConfigPage extends TableConfigPage<AgentParams> {

    public AgentConfigPage(AgentParams[] params) {
        super(params, "Agent Parameters", new TypeColorEnumeration(params));
    }
}
