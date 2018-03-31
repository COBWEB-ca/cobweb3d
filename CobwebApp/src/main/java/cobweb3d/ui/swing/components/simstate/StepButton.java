package cobweb3d.ui.swing.components.simstate;


import cobweb3d.SimulationRunner;
import cobwebutil.MaterialColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This class represents the button responsible for advancing
 * the application by one time tick.
 *
 * @author skinawy
 */
public class StepButton extends JButton implements ActionListener {
    public static final long serialVersionUID = 0xD4B844C0AA5E3991L;
    private SimulationRunner scheduler;

    public StepButton(SimulationRunner scheduler) {
        this();
        setScheduler(scheduler);
    }

    public StepButton() {
        super("Step");
        addActionListener(this);
        setBackground(MaterialColor.yellow_300.asAWTColor());
        setPreferredSize(new Dimension(64, getPreferredSize().height));
    }

    public void setScheduler(SimulationRunner scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (scheduler != null) scheduler.step();
    }
}