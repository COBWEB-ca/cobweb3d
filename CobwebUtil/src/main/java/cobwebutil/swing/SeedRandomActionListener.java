/**
 *
 */
package cobwebutil.swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SeedRandomActionListener implements ActionListener {
    private final JFormattedTextField box;
    private Random random = new Random();

    public SeedRandomActionListener(JFormattedTextField box) {
        this.box = box;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        box.setValue(Math.abs(random.nextLong() % 100000l));
    }
}