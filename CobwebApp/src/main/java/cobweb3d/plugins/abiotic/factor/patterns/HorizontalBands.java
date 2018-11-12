package cobweb3d.plugins.abiotic.factor.patterns;

import cobweb3d.plugins.abiotic.factor.AbioticFactor;

import java.util.ArrayList;



public class HorizontalBands extends Bands {

    @Override
    public float getValue(float x, float y, float z) {
        int band = bandFromPosition(y);
        return bands.get(band).floatValue();
    }

    @Override
    public AbioticFactor copy() {
        HorizontalBands result = new HorizontalBands();
        result.bands = new ArrayList<>(this.bands);
        return result;
    }


    @Override
    public String getName() {
        return "Horizontal Bands";
    }

    private static final long serialVersionUID = 1L;
}
