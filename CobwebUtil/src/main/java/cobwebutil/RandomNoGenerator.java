package cobwebutil;

import java.util.Random;

/**
 * Random number generator
 */
public class RandomNoGenerator extends Random {

    private long seed;

    public static final long serialVersionUID = 0x660028115BCBF9CEL;

    public RandomNoGenerator() {
        super();
    }

    public RandomNoGenerator(long seed) {
        super(seed);
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    /**
     * @return a random integer over (m,n]
     */
    public int nextIntRange(int m, int n) {
        return nextInt(n - m) + m;
    }
}
