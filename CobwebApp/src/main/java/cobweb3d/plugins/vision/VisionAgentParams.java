package cobweb3d.plugins.vision;

import cobwebutil.io.ParameterSerializable;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;

public class VisionAgentParams implements ParameterSerializable {
    private static final long serialVersionUID = 13L;

    @ConfXMLTag("frontEyesight")
    @ConfDisplayName("Seeable front area")
    public int frontEyesight = 3;

    @ConfXMLTag("backEyesight")
    @ConfDisplayName("Seeable back area")
    public int backEyesight = 1;

    @ConfXMLTag("upEyesight")
    @ConfDisplayName("Seeable upper area")
    public int upEyesight = 1;

    @ConfXMLTag("downEyesight")
    @ConfDisplayName("Seeable lower area")
    public int downEyesight = 1;

    public VisionAgentParams() {
    }
}
