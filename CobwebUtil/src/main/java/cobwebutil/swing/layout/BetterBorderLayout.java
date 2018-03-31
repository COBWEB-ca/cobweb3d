package cobwebutil.swing.layout;

import cobwebutil.MaterialColor;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * A modified version of BorderLayout that adds a 1px thin divider between CENTER and other sides.
 *
 * @author Adam Adli
 */
public class BetterBorderLayout extends BorderLayout {

    private Color borderColor = MaterialColor.grey_500.asAWTColor();

    public BetterBorderLayout() {
    }

    public BetterBorderLayout(Color color) {
        this.borderColor = color;
    }

    public BetterBorderLayout(int hgap, int vgap) {
        super(hgap, vgap);
    }

    public BetterBorderLayout(int hgap, int vgap, Color color) {
        super(hgap, vgap);
        this.borderColor = color;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (comp instanceof JComponent) {
            if (constraints == BorderLayout.NORTH)
                ((JComponent) comp).setBorder(new MatteBorder(0, 0, 1, 0, borderColor));
            if (constraints == BorderLayout.SOUTH)
                ((JComponent) comp).setBorder(new MatteBorder(1, 0, 0, 0, borderColor));
            if (constraints == BorderLayout.WEST)
                ((JComponent) comp).setBorder(new MatteBorder(0, 0, 0, 1, borderColor));
            if (constraints == BorderLayout.EAST)
                ((JComponent) comp).setBorder(new MatteBorder(0, 1, 0, 0, borderColor));
        }
        super.addLayoutComponent(comp, constraints);
    }
}
