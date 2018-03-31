package cobweb3d.ui.swing.dialogs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CreditsDialog {

    /**
     * Creates a dialog box with contact information about a specified person
     * in the credits menu.
     *
     * @param parentDialog The credits dialog box that invoked the creation of this dialog box
     * @param S            The contact information that will be shown in the dialog box.
     * @param length       The length of the dialog box in pixels
     * @param width        The width of the dialog box in pixels
     */
    private static void creditDialog(JDialog parentDialog, String[] S, int length, int width) {

        final JDialog creditDialog = new JDialog(parentDialog,
                "Click on Close to continue", true);

        JPanel credit = new JPanel();
        for (int i = 0; i < S.length; ++i) {
            credit.add(new JLabel(S[i]), "Center");
        }

        JPanel term = new JPanel();
        JButton close = new JButton("Close");
        term.add(close);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                creditDialog.setVisible(false);
            }
        });

        creditDialog.add(credit, "Center");
        creditDialog.add(term, "South");

        creditDialog.setSize(length, width);
        creditDialog.setVisible(true);

    }

    /**
     * The credits dialog box that is created when the user selects "Credits"
     * located under "Help" in the main tool bar.  It contains a list of
     * buttons for important people that can be contacted for more information
     * about Cobweb.  The information can be accessed by clicking on the buttons.
     */
    public static void show() {
        final JDialog dialog = new JDialog();
        dialog.setTitle("Credits");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel credit = new JPanel();
        JButton brad = new JButton("Brad Bass, PhD");
        JButton adam = new JButton("Adam Adli");
        credit.add(new JLabel("Coordinator"));
        credit.add(brad);
        //credit.add(new JSeparator(JSeparator.VERTICAL));
        credit.add(new JLabel("Programmer"));
        credit.add(adam);

        JPanel term = new JPanel();
        JButton close = new JButton("Close");
        term.add(close);

        brad.addActionListener(e -> {
            String[] S = {"Brad Bass, PhD", "Adaptations and Impacts Research Group",
                    "Environment Canada at Univ of Toronto", "Inst. for Environmental Studies",
                    "33 Willcocks Street", "Toronto, Ont M5S 3E8 CANADA",
                    "TEL: (416) 978-6285  FAX: (416) 978-3884", "brad.bass@ec.gc.ca"};
            creditDialog(dialog, S, 300, 200);
        });

        adam.addActionListener(e -> {
            String[] S = {"Programmed by", "", "Adam Adli", "adam@adli.ca"};
            creditDialog(dialog, S, 250, 100);
        });
        close.addActionListener(e -> dialog.setVisible(false));
        dialog.add(credit, "Center");
        dialog.add(term, "South");
        dialog.setSize(150, 265);
        dialog.pack();
        dialog.setVisible(true);
    }

}
