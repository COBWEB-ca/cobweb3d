package cobweb3d.impl.logging.strategies.printwriter;

public class PlainTextSavingStategy extends CSVSavingStrategy {
    public PlainTextSavingStategy() {
        super('\t');
    }
}