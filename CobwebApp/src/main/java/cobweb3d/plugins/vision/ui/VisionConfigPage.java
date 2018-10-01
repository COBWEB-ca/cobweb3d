package cobweb3d.plugins.vision.ui;

import cobweb3d.plugins.vision.VisionAgentParams;
import cobweb3d.plugins.vision.VisionParams;
import cobweb3d.ui.swing.config.TableConfigPage;
import cobwebutil.io.ChoiceCatalog;
import cobwebutil.swing.ColorLookup;

public class VisionConfigPage extends TableConfigPage<VisionAgentParams>{
    public VisionConfigPage(VisionParams params, ChoiceCatalog catalog, ColorLookup agentColors) {
        super(params.agentParams, "Vision Parameters", agentColors, catalog);
    }
}
