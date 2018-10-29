package cobweb3d.plugins.abiotic.factor.patterns;

import cobweb3d.plugins.abiotic.factor.AbioticFactor;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfList;
import cobwebutil.io.ConfListType;
import cobwebutil.io.ConfXMLTag;

import java.util.ArrayList;
import java.util.List;

public abstract class Bands extends AbioticFactor {

    @ConfDisplayName("Band")
    @ConfXMLTag("Bands")
    @ConfList(indexName = "Band", startAtOne = true)
    @ConfListType(float.class)
    public List<Float> bands = new ArrayList<>();

    public Bands() {
        bands.add(0f);
        bands.add(1f);
    }

    protected int bandFromPosition(float floatPosition) {
        int size = bands.size();
        int band = (int)(size * floatPosition);
        return band;
    }

    @Override
    public float getMax() {
        float max = Float.NEGATIVE_INFINITY;
        for (Float b : bands) {
            if (b.floatValue() > max)
                max = b;
        }
        return max;
    }

    @Override
    public float getMin() {
        float min = Float.POSITIVE_INFINITY;
        for (Float b : bands) {
            if (b.floatValue() < min)
                min = b;
        }
        return min;
    }

    private static final long serialVersionUID = 1L;
}
