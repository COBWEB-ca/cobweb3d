package cobwebutil.swing;

import java.awt.*;

/**
 * ColorLookup is an interface for mapping numbers inside of finite ranges to
 * colors
 */
public interface ColorLookup {
    Color getColor(int index, int num);
}
