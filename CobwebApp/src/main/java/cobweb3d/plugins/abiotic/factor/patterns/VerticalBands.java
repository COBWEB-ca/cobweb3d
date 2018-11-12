package cobweb3d.plugins.abiotic.factor.patterns;

import cobweb3d.plugins.abiotic.factor.AbioticFactor;

import java.util.ArrayList;

public class VerticalBands extends Bands {
    @Override
    public float getValue(float x, float y, float z) {
        int band = bandFromPosition(x);
        return bands.get(band).floatValue();
    }

    @Override
    public String getName() {
        return "Vertical Bands";
    }

    @Override
    public AbioticFactor copy() {
        VerticalBands result = new VerticalBands();
        result.bands = new ArrayList<>(this.bands);
        return result;
    }
}
