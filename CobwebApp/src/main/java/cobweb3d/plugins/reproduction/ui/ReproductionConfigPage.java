package cobweb3d.plugins.reproduction.ui;

import cobweb3d.plugins.reproduction.ReproductionAgentParams;
import cobweb3d.plugins.reproduction.ReproductionParams;
import cobweb3d.ui.swing.config.TableConfigPage;
import cobwebutil.io.ChoiceCatalog;
import cobwebutil.swing.ColorLookup;

public class ReproductionConfigPage extends TableConfigPage<ReproductionAgentParams> {

    public ReproductionConfigPage(ReproductionParams params, ChoiceCatalog catalog, ColorLookup agentColors) {
        super(params.agentParams, "Reproduction Parameters", agentColors, catalog);
    }
}
