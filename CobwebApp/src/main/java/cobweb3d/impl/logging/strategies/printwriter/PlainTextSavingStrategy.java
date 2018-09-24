package cobweb3d.impl.logging.strategies.printwriter;

/**
 * This class use tab to split items in a same line.
 */
public class PlainTextSavingStrategy extends CSVSavingStrategy {
    public PlainTextSavingStrategy() {
        super('\t');
    }
}