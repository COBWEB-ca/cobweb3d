package cobwebutil;

/**
 * The BitField class is used to store several numbers in a field of bits. This class is used for,
 * among other things, combining different input numbers into a single index number for use with
 * BehaviorArray
 * <p>
 * BitField is first-in, last-out.
 */

public class BitField {

    private int value;

    public BitField() {
        value = 0;
    }

    public BitField(int v) {
        value = v;
    }

    /**
     * The add method shifts the current value left according to the second parameter to make room for
     * that many bits, then places the bits specified by the first parameter into the newly freed
     * slots
     */
    public void add(int val, int bits) {
        val &= (1 << bits) - 1;
        value = (value << bits) | val;
    }

    public int intValue() {
        return value;
    }

    /**
     * The remove method performs the opposite operation of the add method. Remove takes a number of
     * bits specified by the first parameter and removes that many bits from value, returning them.
     *
     * @return the lower 'bits' bits of the value in BitField
     */
    public int remove(int bits) {
        int ret = value & ((1 << bits) - 1);
        value >>>= bits;
        return ret;
    }
}
