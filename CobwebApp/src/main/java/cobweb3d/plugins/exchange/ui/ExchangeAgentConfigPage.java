package cobweb3d.plugins.exchange.ui;

import cobweb3d.plugins.exchange.ExchangeAgentParams;
import cobweb3d.plugins.exchange.ExchangeParams;
import cobweb3d.plugins.exchange.utility.UtilityFunctionParam;
import cobweb3d.ui.swing.config.TableConfigPage;
import cobwebutil.swing.ColorLookup;

public class ExchangeAgentConfigPage extends TableConfigPage<ExchangeAgentParams> {

    public ExchangeAgentConfigPage(ExchangeParams params, ColorLookup agentColors) {
        super(params.agentParams, "Exchange Parameters", agentColors, new UtilityFunctionParam.FormulaChoiceCatalogue());
    }
}
