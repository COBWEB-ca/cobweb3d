package cobweb3d.plugins.abiotic.ui;

import cobweb3d.plugins.abiotic.AbioticAgentParams;
import cobweb3d.plugins.abiotic.AbioticParams;
import cobweb3d.ui.swing.config.TableConfigPage;
import cobwebutil.io.ChoiceCatalog;
import cobwebutil.swing.ColorLookup;

public class AbioticAgentConfigPage extends TableConfigPage<AbioticAgentParams> {
    public AbioticAgentConfigPage(AbioticParams params, ChoiceCatalog catalog, ColorLookup agentColors) {
        super(params.agentParams, "Abiotic Agent Parameters", agentColors, catalog);
    }
}
