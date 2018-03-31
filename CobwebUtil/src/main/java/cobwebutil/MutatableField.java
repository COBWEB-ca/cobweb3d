package cobwebutil;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows stacking multiplier Factors on top of a value. Factors have a Cause attached so they can
 * be updated or removed.
 */
public abstract class MutatableField {

    private final Map<Object, Float> multipliers = new HashMap<>();

    /**
     * Adds/updates a multiplier identified by cause
     *
     * @param cause  The source/cause of factor being applied
     * @param factor multiplication factor
     */
    public void setMultiplier(Object cause, float factor) {
        multipliers.put(cause, factor);
        updateCache();
    }

    /**
     * Removes a multiplier identified by cause
     *
     * @param cause The source/cause of factor being removed
     */
    public void removeMultiplier(Object cause) {
        multipliers.remove(cause);
        updateCache();
    }

    protected float multiplier = 1;

    private void updateCache() {
        multiplier = 1;
        for (Float m : multipliers.values()) {
            multiplier *= m;
        }
    }

}
