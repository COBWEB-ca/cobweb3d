package cobweb3d.plugins.personality;

import cobwebutil.io.CloneHelper;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

public class PersonalityAgentParams implements ParameterSerializable {
    @ConfDisplayName("Personalities Enabled")
    @ConfXMLTag("PersonalitiesEnabled")
    public boolean personalitiesEnabled = true;

    /**
     * Agreeableness : individuals who are agreeable are empathetic, understanding, and unconfrontational.
     * Agreeable individuals could be subject to exploitation by less agreeable individuals.
     *
     * (-participate in prisoner’s dilemma and usually lose
     * -100% loss when manipulated by high “consciousness” individuals)
     */
    @ConfDisplayName("Personalities: Agreeableness")
    @ConfXMLTag("Agreeableness")
    public float agreeableness = 0f;

    /**
     * Consciousness : individuals who are conscious are disciplined and usually successful,
     * these individuals will rise to the top of their hierarchy and if paired with low agreeableness,
     * these tend to rule.
     *
     * (-participate in prisoner’s dilemma and almost always profit from
     * -100% win when faced with “agreeableness” agent)
     */
    @ConfDisplayName("Personalities: Consciousness")
    @ConfXMLTag("Consciousness")
    public float consciousness = 0f;

    /**
     * Openness to Experience : Individuals exhibiting high levels of this trait tend to be creative,
     * intelligent, and curious. Perhaps these agents may be likeable and attract others.
     *
     * (-Attract many ppl
     * -Don’t participate in prisoner’s dilemma)
     */
    @ConfDisplayName("Personalities: Openness")
    @ConfXMLTag("Openness")
    public float openness = 0f;

    /**
     * Neuroticism : individuals who are neurotic tend to be anxious and depressed,
     * perhaps even increasingly angry, making them vulnerable.
     *
     * (-opposite of openness
     * -not usually in groups of many ppl.
     * -usually alone/ppl not attracted)
     */
    @ConfDisplayName("Personalities: Neuroticism")
    @ConfXMLTag("Neuroticism")
    public float neuroticism = 0f;

    /**
     * Individuals who are extraverted are more interactive with other agents.
     * This may result in more alliances, but simultaneously, more conflicts.
     *
     * (-group together and participate in prisoners dilemma
     * -kind of a combination of “openness” and “agreeableness” agents)
     */
    @ConfDisplayName("Personalities: Extroversion")
    @ConfXMLTag("Extroversion")
    public float extroversion = 0f;

    @Override
    protected PersonalityAgentParams clone() {
        try {
            PersonalityAgentParams copy = (PersonalityAgentParams) super.clone();
            CloneHelper.resetMutatable(copy);
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static final long serialVersionUID = 1L;
}
