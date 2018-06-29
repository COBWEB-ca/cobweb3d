package cobweb3d.impl.logging.strategies.printwriter;

/**
 * This class use tab to split items in a same line.
 */
public class PlainTextSavingStategy extends CSVSavingStrategy {
    public PlainTextSavingStategy() {
        super('\t');
    }
}