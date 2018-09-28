package cobweb3d.plugins.resources.ui;

import cobweb3d.plugins.resources.ResourceAgentParams;
import cobweb3d.plugins.resources.ResourceParams;
import cobweb3d.ui.swing.config.TableConfigPage;
import cobwebutil.io.ChoiceCatalog;
import cobwebutil.swing.ColorLookup;

public class ResourceConfigPage extends TableConfigPage<ResourceAgentParams>{

    public ResourceConfigPage(ResourceParams params, ChoiceCatalog catalog, ColorLookup agentColors) {
        super(params.agentParams, "Resource Parameters", agentColors, catalog);
    }
}
