package cobweb3d.ui.swing.dialogs;

import cobweb3d.BuildConfig;

import javax.swing.*;
import java.awt.*;

public class AboutDialog {

    /**
     * Creates the about dialog box, which contains information pertaining
     * to the Cobweb version being used, and the date it was last modified.
     */
    public static void show() {
        final javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("About COBWEB 3D");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel info = new JPanel();
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.add(new JLabel("<html><center>COBWEB 3D 2018<br/>version: <br/>"
                + BuildConfig.VERSION
                + "</center></html>"));

        JPanel term = new JPanel();
        JButton close = new JButton("Close");
        close.addActionListener(e -> dialog.setVisible(false));

        term.add(close);

        dialog.setLayout(new BorderLayout());
        dialog.add(info, BorderLayout.CENTER);
        dialog.add(term, BorderLayout.SOUTH);
        dialog.setSize(300, 150);
        dialog.setVisible(true);
    }
}
