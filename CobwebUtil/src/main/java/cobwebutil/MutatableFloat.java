package cobwebutil;

/**
 * Stores a float originalValue, and allows stacking multiplier Factors on top of it. Factors have a
 * Cause attached so they can be updated or removed.
 *
 * @see MutatableInt
 */
public class MutatableFloat extends MutatableField {

    private float originalValue;

    public MutatableFloat(float value) {
        setValue(value);
    }

    /**
     * Sets value before any factors are applied to it
     */
    public void setValue(float value) {
        this.originalValue = value;
    }

    /**
     * Gets value with all the factors applied to it
     */
    public float getValue() {
        return originalValue * multiplier;
    }

    /**
     * Gets value before any factors are applied
     */
    public float getRawValue() {
        return originalValue;
    }
}
