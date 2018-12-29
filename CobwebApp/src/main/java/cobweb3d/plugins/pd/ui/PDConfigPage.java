package cobweb3d.plugins.pd.ui;

import cobweb3d.plugins.pd.PDAgentParams;
import cobweb3d.plugins.pd.PDParams;
import cobweb3d.ui.swing.config.TwoTableConfigPage;
import cobwebutil.swing.ColorLookup;

public class PDConfigPage extends TwoTableConfigPage<PDParams, PDAgentParams> {
    public PDConfigPage(PDParams params, ColorLookup colors) {
        super(PDParams.class, params, "Prisoner's Dilemma Parameters", colors);
    }
}
