package cobwebutil.math;

import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class VectorUtil {
    public static Vector2f getXZAngles(Vector3f direction, Vector3f up) {
        if (direction == null) return new Vector2f(0, 0);
        Vector3f diff = up == null ? direction : up.sub(direction, new Vector3f());
        float angleX = (float) Math.toDegrees(Math.asin(diff.z));
        float angleZ = (float) Math.toDegrees(Math.atan2(diff.y, diff.x) * 2);
        return new Vector2f(angleX, angleZ);
    }
}
