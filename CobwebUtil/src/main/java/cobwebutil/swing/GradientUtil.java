package cobwebutil.swing;

import java.awt.*;


public class GradientUtil {
    public static Color colorFromFloat(float y) {
        return new Color(Color.HSBtoRGB((1 - y) * 2 / 3, 1f, 1f));
    }

    public static Color colorFromFloat(float y, float alpha) {
        int a = (int) (alpha * 255);
        int intCol =
                Color.HSBtoRGB((1 - y) * 2 / 3, 1f, 1f) & 0xffffff |
                        (a << 24);
        return new Color(intCol, true);
    }
}
