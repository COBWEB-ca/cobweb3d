package cobweb3d.plugins.reproduction;

import cobwebutil.MutatableFloat;
import cobwebutil.MutatableInt;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

public class ReproductionAgentParams implements ParameterSerializable {
    private static final long serialVersionUID = 12L;
    /**
     * Chance that bumping into another agent will result in sexual breeding.
     */
    @ConfDisplayName("Sexual reproduction chance")
    @ConfXMLTag("sexualBreedChance")
    public MutatableFloat sexualBreedChance = new MutatableFloat(1);
    /**
     * Chance an agent breeds asexually at a time step.
     */
    @ConfDisplayName("Asexual reproduction chance")
    @ConfXMLTag("asexualBreedChance")
    public MutatableFloat asexualBreedChance = new MutatableFloat(0);
    /**
     * Amount of energy used to reproduction.
     */
    @ConfDisplayName("Breed energy")
    @ConfXMLTag("BreedEnergy")
    public MutatableInt breedEnergy = new MutatableInt(60);
    /**
     * Time between sexual breeding and producing child agent.
     */
    @ConfDisplayName("Sexual pregnancy period")
    @ConfXMLTag("sexualPregnancyPeriod")
    public MutatableInt sexualPregnancyPeriod = new MutatableInt(5);

    /**
     * Time between asexual breeding and producing child agent.
     */
    @ConfDisplayName("Asexual pregnancy period")
    @ConfXMLTag("pregnancyPeriod")
    public MutatableInt asexPregnancyPeriod = new MutatableInt(0);

    //@Deprecated // for reflection use only!
    public ReproductionAgentParams() {
    }
}