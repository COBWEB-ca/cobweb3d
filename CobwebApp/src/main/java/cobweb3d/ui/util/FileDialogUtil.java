package cobweb3d.ui.util;

import cobwebutil.swing.FileExtFilter;
import cobwebutil.swing.jfx.SynchronousJFXFileChooser;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileDialogUtil {

    @Nullable
    public static File openFile(Window parent, String title, FileExtFilter... fileExtFilters) {
        JFileChooser openDialog = new JFileChooser();
        openDialog.setDialogTitle(title);
        openDialog.setDialogType(JFileChooser.OPEN_DIALOG);
        openDialog.setAcceptAllFileFilterUsed(false);
        for (FileExtFilter fileExtFilter : fileExtFilters)
            openDialog.addChoosableFileFilter(fileExtFilter);

        openDialog.setVisible(true);
        if (openDialog.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = openDialog.getSelectedFile();
            if (file.exists()) {
                return file;
            } else {
                JOptionPane.showMessageDialog(
                        parent,
                        "File \"" + file.getAbsolutePath() + file + "\" could not be found!", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        return null;
    }

    @Nullable
    public static String saveFile(Window parent, String title, FileExtFilter... fileExtFilters) {
        JFileChooser saveDialog = new JFileChooser();
        saveDialog.setDialogTitle(title);
        saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);
        saveDialog.setAcceptAllFileFilterUsed(false);
        for (FileExtFilter fileExtFilter : fileExtFilters)
            saveDialog.addChoosableFileFilter(fileExtFilter);

        saveDialog.setVisible(true);
        if (saveDialog.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            return saveDialog.getSelectedFile().getAbsolutePath();
        return null;
    }

    @Nullable
    public static File openFileJFX(Window parent, String title, FileChooser.ExtensionFilter... fileExtFilters) {
        File file = launchJFXFileChooser(true, parent, title, fileExtFilters);
        if (file != null) {
            if (file.exists()) {
                return file;
            } else {
                JOptionPane.showMessageDialog(
                        parent,
                        "File \"" + file.getAbsolutePath() + file + "\" could not be found!", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }
        return null;
    }

    @Nullable
    public static String saveFileJFX(Window parent, String title, FileChooser.ExtensionFilter... fileExtFilters) {
        File file = launchJFXFileChooser(false, parent, title, fileExtFilters);
        if (file != null) {
            if (file.exists())
                System.out.println("Warning: File \"" + file.getAbsolutePath() + file + "\" already exists, overwriting!");
            return file.getAbsolutePath();
        }
        return null;
    }

    private static File launchJFXFileChooser(boolean open, Window parent, String title, FileChooser.ExtensionFilter... fileExtFilters) {
        SynchronousJFXFileChooser synchronousJFXFileChooser = new SynchronousJFXFileChooser(() -> {
            FileChooser jfxOpenDialog = new FileChooser();
            jfxOpenDialog.setTitle(title);
            jfxOpenDialog.getExtensionFilters().clear();
            jfxOpenDialog.getExtensionFilters().addAll(fileExtFilters);
            return jfxOpenDialog;
        });
        return open ? synchronousJFXFileChooser.showOpenDialog() : synchronousJFXFileChooser.showSaveDialog();
    }
}
