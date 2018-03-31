package cobwebutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class FileUtils {

    /**
     * Copies the contents from the source file to the destination file.  If the destination file does
     * not exist, it will be created.
     *
     * @param src  The file name of the source file to be copied from
     * @param dest The file name of the destination file to be copied to
     * @throws IOException On failure
     */
    public static void copyFile(String src, String dest) throws IOException {
        File sourceFile = new File(src);
        File destFile = new File(dest);

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static String getFileExtension(File file) {
        if (file == null) return "";
        String fileName = file.getPath();
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }
}
