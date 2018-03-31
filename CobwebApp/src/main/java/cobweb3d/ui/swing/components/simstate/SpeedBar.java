package cobweb3d.ui.swing.components.simstate;

import cobweb3d.ThreadSimulationRunner;
import cobwebutil.MaterialColor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.AdjustmentEvent;

public class SpeedBar extends JScrollBar implements
        java.awt.event.AdjustmentListener {
    public static final long serialVersionUID = 0xD5E78F1D65B18165L;
    private static final int SCROLLBAR_TICKS = 11;
    private ThreadSimulationRunner scheduler;
    private final Color original;

    public SpeedBar(ThreadSimulationRunner scheduler) {
        this();
        setScheduler(scheduler);
    }

    public SpeedBar() {
        setOrientation(Adjustable.HORIZONTAL);
        addAdjustmentListener(this);
        setValues(SCROLLBAR_TICKS - 1, 0, 0, SCROLLBAR_TICKS);
        setPreferredSize(new Dimension(92, getPreferredSize().height));
        setMaximumSize(new Dimension(92, getPreferredSize().height));
        setBorder(new EmptyBorder(-1, 0, -1, 0));
        original = this.getBackground();
    }

    public void setScheduler(ThreadSimulationRunner scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        int delay = 0;
        int d1 = SCROLLBAR_TICKS - getValue();
        if (d1 != 0) {
            delay = 1 << (d1 - 1);
        }
        if (delay == 0) {
            this.setBackground(MaterialColor.green_300.asAWTColor());
        } else {
            this.setBackground(original);
        }
        if (scheduler != null) scheduler.setDelay(delay);
    }
}