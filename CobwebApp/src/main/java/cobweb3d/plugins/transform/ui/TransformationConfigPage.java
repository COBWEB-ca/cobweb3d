package cobweb3d.plugins.transform.ui;

import cobweb3d.plugins.transform.TransformationAgentParams;
import cobweb3d.plugins.transform.TransformationParams;
import cobweb3d.ui.swing.config.TableConfigPage;
import cobwebutil.io.ChoiceCatalog;
import cobwebutil.swing.ColorLookup;

public class TransformationConfigPage extends TableConfigPage<TransformationAgentParams> {

    public TransformationConfigPage(TransformationParams params, ChoiceCatalog catalog, ColorLookup agentColors) {
        super(params.agentParams, "Transformation Parameters", agentColors, catalog);
    }
}
