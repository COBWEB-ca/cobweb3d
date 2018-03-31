package cobweb3d.plugins.ported.disease.ui;

import cobweb3d.plugins.ported.disease.DiseaseAgentParams;
import cobweb3d.plugins.ported.disease.DiseaseParams;
import cobweb3d.ui.swing.config.TableConfigPage;
import cobwebutil.io.ChoiceCatalog;
import cobwebutil.swing.ColorLookup;

/**
 * Configuration page for Disease
 */
public class DiseaseConfigPage extends TableConfigPage<DiseaseAgentParams> {

    public DiseaseConfigPage(DiseaseParams params, ChoiceCatalog catalog, ColorLookup agentColors) {
        super(params.agentParams, "Disease Parameters", agentColors, catalog);
    }
}
