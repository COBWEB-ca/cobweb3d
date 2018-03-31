package cobwebutil.javafx;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;

public class YUpXForm extends Group {
    public Rotate rx = new Rotate();
    public Rotate ry = new Rotate();
    public Rotate rz = new Rotate();

    {
        rx.setAxis(Rotate.X_AXIS);
    }

    {
        ry.setAxis(Rotate.Y_AXIS);
    }

    {
        rz.setAxis(Rotate.Z_AXIS);
    }

    public YUpXForm() {
        super();
        getTransforms().addAll(rz, ry, rx);
    }

    public YUpXForm(RotateOrder rotateOrder) {
        super();
        // choose the order of rotations based on the rotateOrder
        switch (rotateOrder) {
            case XYZ:
                getTransforms().addAll(rz, ry, rx);
                break;
            case XZY:
                getTransforms().addAll(ry, rz, rx);
                break;
            case YXZ:
                getTransforms().addAll(rz, rx, ry);
                break;
            case YZX:
                getTransforms().addAll(rx, rz, ry);  // For Camera
                break;
            case ZXY:
                getTransforms().addAll(ry, rx, rz);
                break;
            case ZYX:
                getTransforms().addAll(rx, ry, rz);
                break;
        }
    }

    public void setRotate(double x, double y, double z) {
        rx.setAngle(x);
        ry.setAngle(y);
        rz.setAngle(z);
    }

    public void setRotateX(double x) {
        rx.setAngle(x);
    }

    public void setRotateY(double y) {
        ry.setAngle(y);
    }

    public void setRotateZ(double z) {
        rz.setAngle(z);
    }

    public void setRx(double x) {
        rx.setAngle(x);
    }

    public void setRy(double y) {
        ry.setAngle(y);
    }

    public void setRz(double z) {
        rz.setAngle(z);
    }

    public void reset() {
        rx.setAngle(0.0);
        ry.setAngle(0.0);
        rz.setAngle(0.0);
    }

    public enum RotateOrder {
        XYZ, XZY, YXZ, YZX, ZXY, ZYX
    }
}
