package cobwebutil.swing.jfx;

import javafx.stage.FileChooser;

public class JFXFileExtFilter {
    public static final FileChooser.ExtensionFilter EXCEL_XLSX = new FileChooser.ExtensionFilter("Excel", "*.xlsx");
    public static final FileChooser.ExtensionFilter LOG_TEXT = new FileChooser.ExtensionFilter("Text", "*.log");
    public static final FileChooser.ExtensionFilter LOG_CSV = new FileChooser.ExtensionFilter("CSV", "*.csv");
    public static final FileChooser.ExtensionFilter COBWEB3D_XML = new FileChooser.ExtensionFilter("COBWEB3D Simulation", "*.xml", "*.XML");

    public static final FileChooser.ExtensionFilter LOG_CSV_FASTEST = new FileChooser.ExtensionFilter("CSV (fastest)", "*.csv");
    public static final FileChooser.ExtensionFilter LOG_TEXT_FAST = new FileChooser.ExtensionFilter("Text (fast)", "*.log");
    public static final FileChooser.ExtensionFilter EXCEL_XLSX_SLOWEST = new FileChooser.ExtensionFilter("Excel", "*.xlsx");
}
