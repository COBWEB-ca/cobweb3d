package cobweb3d.ui.swing.components.simstate;

import cobweb3d.SimulationRunner;
import cobwebutil.MaterialColor;

import javax.swing.*;
import java.awt.*;

public class RestartButton extends JButton implements java.awt.event.ActionListener {
    public static final long serialVersionUID = 0xE55CC6E3B8B5824DL;
    private SimulationRunner scheduler;

    public RestartButton(SimulationRunner scheduler) {
        this();
        setScheduler(scheduler);
    }

    public RestartButton() {
        super("Reset");
        addActionListener(this);
        setBackground(MaterialColor.orange_300.asAWTColor());
        setPreferredSize(new Dimension(64, getPreferredSize().height));
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (scheduler != null) scheduler.reset();
        repaint();
    }

    public void setScheduler(SimulationRunner scheduler) {
        this.scheduler = scheduler;
        repaint();
    }
}
