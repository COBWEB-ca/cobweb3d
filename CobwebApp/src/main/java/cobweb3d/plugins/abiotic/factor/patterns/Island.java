package cobweb3d.plugins.abiotic.factor.patterns;

import cobweb3d.plugins.abiotic.factor.AbioticFactor;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ConfDisplayName;

public class Island extends AbioticFactor {

    @ConfXMLTag("center")
    @ConfDisplayName("Island value")
    public float islandValue = 1f;

    @ConfXMLTag("outside")
    @ConfDisplayName("Outside value")
    public float outsideValue = 0f;

    @ConfXMLTag("sizeX")
    @ConfDisplayName("Island size X")
    public float sizeX = 0.5f;

    @ConfXMLTag("sizeZ")
    @ConfDisplayName("Island size Z")
    public float sizeZ = 0.5f;

    @ConfXMLTag("sizeY")
    @ConfDisplayName("Island size Y")
    public float sizeY = 0.5f;

    @ConfXMLTag("positionX")
    @ConfDisplayName("Position X")
    public float positionX = 0.4f;

    @ConfXMLTag("positionY")
    @ConfDisplayName("Position Y")
    public float positionY = 0.5f;

    @ConfXMLTag("positionZ")
    @ConfDisplayName("Position Z")
    public float positionZ = 0.5f;

    @ConfXMLTag("transition")
    @ConfDisplayName("Edge hardness")
    public float transition = 0.3f;

    @Override
    public float getValue(float x, float y, float z) {
        x -= positionX;
        y -= positionY;
        z -= positionZ;

        x /= sizeX / 2;
        y /= sizeY / 2;
        z /= sizeZ / 2;


        float distance = 1 - (x*x + y*y);
        float insidePart =  1.0f / (float) (1 + Math.exp(-distance * 10 * transition));

        float distanceA = 1 -(x*x + z*z);
        float insidePartA =  1.0f / (float) (1 + Math.exp(-distanceA * 10 * transition));

        if (insidePart < 0 || insidePartA < 0) {
            insidePart = 0;
          //  insidePartA = 0;
        }
        else if (insidePart > 1 || distanceA > 1) {
            insidePart = 1;
           // insidePartA = 1;
        }
        return islandValue * insidePart + outsideValue * (1 - insidePart);
    }

    @Override
    public float getMax() {
        return Math.max(islandValue, outsideValue);
    }

    @Override
    public float getMin() {
        return Math.min(islandValue, outsideValue);
    }

    @Override
    public String getName() {
        return "Island";
    }

    @Override
    public AbioticFactor copy() {
        try {
            return (Island) this.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }


    private static final long serialVersionUID = 1L;
}




