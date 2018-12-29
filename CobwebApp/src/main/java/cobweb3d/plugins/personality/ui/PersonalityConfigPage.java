package cobweb3d.plugins.personality.ui;

import cobweb3d.plugins.personality.PersonalityAgentParams;
import cobweb3d.plugins.personality.PersonalityParams;
import cobweb3d.ui.swing.config.TwoTableConfigPage;
import cobwebutil.swing.ColorLookup;

public class PersonalityConfigPage extends TwoTableConfigPage<PersonalityParams, PersonalityAgentParams> {
    public PersonalityConfigPage(PersonalityParams params, ColorLookup agentColors) {
        super(PersonalityParams.class, params, "Personality Parameters", agentColors);
    }
}
