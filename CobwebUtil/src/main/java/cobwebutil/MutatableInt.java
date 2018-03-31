package cobwebutil;

/**
 * Stores a int originalValue, and allows stacking multiplier Factors on top of it. Factors have a
 * Cause attached so they can be updated or removed.
 *
 * @see MutatableFloat
 */
public class MutatableInt extends MutatableField {

    private int originalValue;

    public MutatableInt(int value) {
        setValue(value);
    }

    /**
     * Sets value before any factors are applied to it
     */
    public void setValue(int value) {
        this.originalValue = value;
    }

    /**
     * Gets value with all the factors applied to it
     */
    public int getValue() {
        return Math.round(originalValue * multiplier);
    }

    /**
     * Gets value before any factors are applied
     */
    public int getRawValue() {
        return originalValue;
    }
}
