package cobweb3d.ui.util;

import cobweb3d.impl.params.AgentParams;
import cobwebutil.ArrayUtilities;
import cobwebutil.MaterialColor;
import cobwebutil.swing.ColorLookup;

import java.awt.*;

public class TypeColorEnumeration implements ColorLookup {

    private Color[] colors = new Color[1];

    public TypeColorEnumeration(AgentParams[] agentParams) {
        colors = ArrayUtilities.resizeArray(colors, agentParams.length);
        for (int i = 0; i < agentParams.length; i++) {
            try {
                colors[i] = Color.decode(agentParams[i].color);
            } catch (Exception ex) {
                colors[i] = MaterialColor.rand().asAWTColor();
                agentParams[i].color = String.format("#%02x%02x%02x", colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue());
            }
        }
    }

    @Override
    public Color getColor(int index, int num) {
        // generates any number of colors, num bound is ignored.
        if (colors.length == 0) return Color.WHITE;
        Color c = colors[index % colors.length];
        while (index >= colors.length) {
            index -= colors.length;
            c = c.darker();
        }
        return c;
    }
}
