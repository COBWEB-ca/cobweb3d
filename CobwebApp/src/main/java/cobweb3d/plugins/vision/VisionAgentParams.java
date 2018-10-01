package cobweb3d.plugins.vision;

import cobwebutil.io.ParameterSerializable;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;

public class VisionAgentParams implements ParameterSerializable {
    private static final long serialVersionUID = 13L;

    @ConfXMLTag("frontEyesight")
    @ConfDisplayName("Seeable front distance")
    public int frontEyesight = 3;

    @ConfXMLTag("backEyesight")
    @ConfDisplayName("Seeable back distance")
    public int backEyesight = 1;

    @ConfXMLTag("backEyesight")
    @ConfDisplayName("Seeable left distance")
    public int leftEyesight = 1;

    @ConfXMLTag("rightEyesight")
    @ConfDisplayName("Seeable right distance")
    public int rightEyesight = 1;

    @ConfXMLTag("upEyesight")
    @ConfDisplayName("Seeable upper distance")
    public int upEyesight = 1;

    @ConfXMLTag("downEyesight")
    @ConfDisplayName("Seeable lower distance")
    public int downEyesight = 1;

    public VisionAgentParams() {
    }
}
