package cobwebutil;

public class MathUtil {


    /**
     * Distance from point (x,y) to line going through (0,0) at angle lineAngle Distance is adjusted
     * to touch the bounds of a 1 by 1 square centered at (0,0)
     *
     * @param x         point x
     * @param y         point y
     * @param lineAngle line angle
     * @return signed distance to to line
     */
    public static double pointLineDistInSquare(double x, double y, double lineAngle) {
        // Make sure tan(90) is not approached
        double q = lineAngle / (Math.PI / 4);
        if (q > 1 && q <= 3 || q > -3 && q <= -1) {
            // +45 to +135 or -135 to -45
            double t = x;
            x = -y;
            y = -t;
            lineAngle = Math.PI / 2 - lineAngle;
        }

        double slope = Math.tan(lineAngle);
        // make 90 degree angles completely square
        if (Math.abs(slope) < 1e-5) {
            slope = 0;
        }
        double xZero = -Math.sin(lineAngle);
        double yZero = Math.cos(lineAngle);

        double dist = (y - x * slope) / (yZero - xZero * slope) * yZero;
        return dist;
    }

    public static float clamp(float v, float min, float max) {
        if (v > max) {
            v = max;
        } else if (v < min) {
            v = min;
        }
        return v;
    }

    public static float linearInterpolation(float x, float x0, float x1, float y0, float y1) {
        float t = (x - x0) / (x1 - x0);
        return y1 * t + y0 * (1 - t);
    }
}
