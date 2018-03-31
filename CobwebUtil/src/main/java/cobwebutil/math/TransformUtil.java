package cobwebutil.math;

import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

public abstract class TransformUtil {
    public static void TransformNode(Node node, Vector3f position, float pivotX, float pivotY, float pivotZ, float angleX, float angleZ) {
        node.setTranslateX(position.x);
        node.setTranslateY(position.y);
        node.setTranslateZ(position.z);
        node.getTransforms().add(new Rotate(angleZ, pivotX, pivotY, pivotZ, Rotate.Z_AXIS));
        node.getTransforms().add(new Rotate(angleX, pivotX, pivotY, pivotZ, Rotate.X_AXIS));
    }

    public static void TransformNode(Node node, Vector3f position, float pivotX, float pivotY, float pivotZ, Vector3f direction, Vector3f up) {
        node.setTranslateX(position.x);
        node.setTranslateY(position.y);
        node.setTranslateZ(position.z);
        Vector2f angles = VectorUtil.getXZAngles(new Vector3f(direction).normalize(), up);
        node.getTransforms().add(new Rotate(angles.y, pivotX, pivotY, pivotZ, Rotate.Z_AXIS));
        node.getTransforms().add(new Rotate(angles.x, pivotX, pivotY, pivotZ, Rotate.X_AXIS));
    }

    public static void TransformNode(Node node, Vector3i position, float pivotX, float pivotY, float pivotZ, Vector3i direction, Vector3f up) {
        node.setTranslateX(position.x);
        node.setTranslateY(position.y);
        node.setTranslateZ(position.z);
        Vector2f angles = VectorUtil.getXZAngles(new Vector3f(direction).normalize(), up);
        node.getTransforms().add(new Rotate(angles.y, pivotX, pivotY, pivotZ, Rotate.Z_AXIS));
        node.getTransforms().add(new Rotate(angles.x, pivotX, pivotY, pivotZ, Rotate.X_AXIS));
    }

    public static void TransformNode(Node node, Vector3i position, float pivotX, float pivotY, float pivotZ, Vector3i direction, Vector3f up, Vector3f temp) {
        node.setTranslateX(position.x);
        node.setTranslateY(position.y);
        node.setTranslateZ(position.z);
        temp.set(direction);
        temp.normalize();
        Vector2f angles = VectorUtil.getXZAngles(temp, up);
        node.getTransforms().add(new Rotate(angles.y, pivotX, pivotY, pivotZ, Rotate.Z_AXIS));
        node.getTransforms().add(new Rotate(angles.x, pivotX, pivotY, pivotZ, Rotate.X_AXIS));
    }
}
