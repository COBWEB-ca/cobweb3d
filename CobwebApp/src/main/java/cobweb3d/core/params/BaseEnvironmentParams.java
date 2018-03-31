package cobweb3d.core.params;

import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

public class BaseEnvironmentParams implements ParameterSerializable {

    private static final long serialVersionUID = 2L;
    /**
     * Width of the grid.
     */
    @ConfDisplayName("Width")
    @ConfXMLTag("Width")
    public int width = 10;
    /**
     * Height of the grid.
     */
    @ConfDisplayName("Height")
    @ConfXMLTag("Height")
    public int height = 10;
    /**
     * Height of the grid.
     */
    @ConfDisplayName("Depth")
    @ConfXMLTag("Depth")
    public int depth = 10;
    /**
     * Enables the grid to wrap around at the edges.
     */
    @ConfDisplayName("Wrap edges")
    @ConfXMLTag("wrap")
    public boolean wrapMap = true;
    /**
     * Number of stones to randomly place
     */
    @ConfDisplayName("Random stones")
    @ConfXMLTag("randomStones")
    public int initialStones = 10;
}
