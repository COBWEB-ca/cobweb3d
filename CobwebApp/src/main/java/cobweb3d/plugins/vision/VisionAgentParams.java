package cobweb3d.plugins.vision;

import cobwebutil.io.ParameterSerializable;
import cobwebutil.MutatableInt;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;

public class VisionAgentParams implements ParameterSerializable {
    private static final long serialVersionUID = 13L;

    @ConfXMLTag("frontEyesight")
    @ConfDisplayName("Seeable front area")
    public MutatableInt frontEyesight = new MutatableInt(3);

    @ConfXMLTag("backEyesight")
    @ConfDisplayName("Seeable back area")
    public MutatableInt backEyesight = new MutatableInt(1);

    @ConfXMLTag("upEyesight")
    @ConfDisplayName("Seeable upper area")
    public MutatableInt upEyesight = new MutatableInt(1);

    @ConfXMLTag("downEyesight")
    @ConfDisplayName("Seeable lower area")
    public MutatableInt downEyesight = new MutatableInt(1);

    public VisionAgentParams() {
    }
}
