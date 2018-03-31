package cobweb3d.plugins.diminish.ui;

import cobweb3d.plugins.diminish.DiminishAgentParams;
import cobweb3d.plugins.diminish.DiminishParams;
import cobweb3d.ui.swing.config.TableConfigPage;
import cobwebutil.io.ChoiceCatalog;
import cobwebutil.swing.ColorLookup;

public class DiminishConfigPage extends TableConfigPage<DiminishAgentParams> {

    public DiminishConfigPage(DiminishParams params, ChoiceCatalog catalog, ColorLookup agentColors) {
        super(params.agentParams, "Diminish Parameters", agentColors, catalog);
    }
}
