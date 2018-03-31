package cobweb3d.rendering.javafx;

import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Affine;
import org.joml.Vector3f;

public class SimCamera extends PerspectiveCamera implements ISimCamera {
    private float aspectRatio = 1f;
    private float fovY = 45.0f;
    private float fovX = 45.0f;
    private float fovYr = 45.0f;
    private float fovXr = 45.0f;
    private double nearClip = 0.1;
    private double farClip = 10000.0;

    public SimCamera() {
        this(true);
    }

    public SimCamera(boolean fixedEyeAtCameraZero) {
        super(fixedEyeAtCameraZero);
        setNearClip(nearClip);
        setFarClip(farClip);
        setFieldOfView(fovY);
    }

    private static Affine lookAtCalc(Vector3f from, Vector3f to, Vector3f ydir) {
        Vector3f zVec = to.sub(from, new Vector3f()).normalize();
        Vector3f xVec = ydir.normalize().cross(zVec, new Vector3f()).normalize();
        Vector3f yVec = zVec.cross(xVec, new Vector3f()).normalize();
        return new Affine(xVec.x, yVec.x, zVec.x, from.x,
                xVec.y, yVec.y, zVec.y, from.y,
                xVec.z, yVec.z, zVec.z, from.z);
    }

    private static Affine lookAtCalc(Point3D from, Point3D to, Point3D ydir) {
        Point3D zVec = to.subtract(from).normalize();
        Point3D xVec = ydir.normalize().crossProduct(zVec).normalize();
        Point3D yVec = zVec.crossProduct(xVec).normalize();
        return new Affine(xVec.getX(), yVec.getX(), zVec.getX(), from.getX(),
                xVec.getY(), yVec.getY(), zVec.getY(), from.getY(),
                xVec.getZ(), yVec.getZ(), zVec.getZ(), from.getZ());
    }

    public SimCamera adjustForResolution(int width, int height) {
        aspectRatio = ((float) width) / ((float) height);
        fovYr = (float) Math.toRadians(fovY);
        fovXr = 2 * (float) Math.atan(Math.tan(fovYr / 2) * aspectRatio);//fovYr * aspect; //2 * (float) Math.atan(Math.tan(fovYr / 2) * aspect);
        fovX = (float) Math.toDegrees(fovXr);
        setNearClip(nearClip);
        setFarClip(farClip);
        setFieldOfView(fovY);
        return this;
    }

    public SimCamera lookAt(Point3D from, Point3D to, Point3D ydir) {
        getTransforms().clear();
        getTransforms().add(lookAtCalc(from, to, ydir));
        return this;
    }

    public SimCamera lookAt(Vector3f from, Vector3f to, Vector3f ydir) {
        getTransforms().clear();
        getTransforms().add(lookAtCalc(from, to, ydir));
        return this;
    }

    public float aspectRatio() {
        return aspectRatio;
    }

    public float fovY() {
        return fovY;
    }

    public float fovX() {
        return fovX;
    }

    public float fovYr() {
        return fovYr;
    }

    public float fovXr() {
        return fovXr;
    }

    public double nearClip() {
        return nearClip;
    }

    public double farClip() {
        return farClip;
    }
}
