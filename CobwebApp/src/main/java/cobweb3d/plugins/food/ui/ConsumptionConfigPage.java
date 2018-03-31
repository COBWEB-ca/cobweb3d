package cobweb3d.plugins.food.ui;

import cobweb3d.plugins.food.ConsumptionAgentParams;
import cobweb3d.plugins.food.ConsumptionParams;
import cobweb3d.ui.swing.config.TableConfigPage;
import cobwebutil.io.ChoiceCatalog;
import cobwebutil.swing.ColorLookup;

public class ConsumptionConfigPage extends TableConfigPage<ConsumptionAgentParams> {

    public ConsumptionConfigPage(ConsumptionParams params, ChoiceCatalog catalog, ColorLookup agentColors) {
        super(params.agentParams, "Consumption Parameters", agentColors, catalog);
    }
}
