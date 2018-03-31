package cobwebutil.swing;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileExtFilter extends FileFilter {

    public static final FileExtFilter EXCEL_XLSX = new FileExtFilter("xlsx", "Excel");
    public static final FileExtFilter TEXT_LOG = new FileExtFilter("log", "Text Log");
    public static final FileExtFilter LOG_CSV = new FileExtFilter("csv", "Log");
    public static final FileExtFilter COBWEB3D_XML = new FileExtFilter("xml", "COBWEB3D");
    public String description = null;
    public String fileExt = "*";

    public FileExtFilter(String extension) {
        this(extension, null);
    }

    public FileExtFilter(String extension, String typeDescription) {
        if (extension != null) fileExt = extension;
        this.description = typeDescription;
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory())
            return true;
        return (f.getName().toLowerCase().endsWith(fileExt));
    }

    @Override
    public String getDescription() {
        if (description == null) return "." + fileExt;
        else return description + " (." + fileExt + ")";
    }
}