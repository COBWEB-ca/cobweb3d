package cobweb3d.rendering.javafx;

import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import org.joml.Vector3f;

public interface ISimCamera {
    static Affine lookAtCalc(Vector3f from, Vector3f to, Vector3f ydir) {
        Vector3f zVec = to.sub(from, new Vector3f()).normalize();
        Vector3f xVec = ydir.normalize().cross(zVec, new Vector3f()).normalize();
        Vector3f yVec = zVec.cross(xVec, new Vector3f()).normalize();
        return new Affine(xVec.x, yVec.x, zVec.x, from.x,
                xVec.y, yVec.y, zVec.y, from.y,
                xVec.z, yVec.z, zVec.z, from.z);
    }

    static Affine lookAtCalc(Point3D from, Point3D to, Point3D ydir) {
        Point3D zVec = to.subtract(from).normalize();
        Point3D xVec = ydir.normalize().crossProduct(zVec).normalize();
        Point3D yVec = zVec.crossProduct(xVec).normalize();
        return new Affine(xVec.getX(), yVec.getX(), zVec.getX(), from.getX(),
                xVec.getY(), yVec.getY(), zVec.getY(), from.getY(),
                xVec.getZ(), yVec.getZ(), zVec.getZ(), from.getZ());
    }

    ISimCamera adjustForResolution(int width, int height);

    ISimCamera lookAt(Point3D from, Point3D to, Point3D ydir);

    ISimCamera lookAt(Vector3f from, Vector3f to, Vector3f ydir);
}
