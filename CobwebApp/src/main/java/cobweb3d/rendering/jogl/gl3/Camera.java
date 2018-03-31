package cobweb3d.rendering.jogl.gl3;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    public Matrix4f projectionMatrix;
    public Matrix4f viewMatrix;
    public Matrix3f rotationMatrix;

    // Projection Matrix properties
    public float fovY = 45.0f;
    public float fovX = 45.0f;
    public float aspectRatio = 1;
    public float nearPlane = 0.1f;
    public float farPlane = 1000f;

    public Vector3f position;
    private float pitch, roll, yaw = 0;
    public Vector3f direction;

    public Vector3f target;
    public float distanceFromTarget = 40;

    public Camera() {
        position = new Vector3f(0, 0, 0);
    }

    public void createMatrices() {
        rotationMatrix = new Matrix3f().rotationXYZ(pitch, yaw, roll);
        direction = new Vector3f(0, 0, -1);
        rotationMatrix.transform(direction);

        Vector3f upVector = rotationMatrix.transform(new Vector3f(0, 1, 0));
        Vector3f centerViewVector = new Vector3f();
        position.add(direction, centerViewVector);

        viewMatrix = new Matrix4f().lookAtLH(position, centerViewVector, upVector);
        projectionMatrix = new Matrix4f().perspectiveLH(fovY, aspectRatio, nearPlane, farPlane);

        if (target != null) {

            direction = new Vector3f(0, 0, -1);

            // projectionMatrix = new Matrix4f().lookAtLH()
        }
    }

    public void calculateCameraPosition() {

    }
}
