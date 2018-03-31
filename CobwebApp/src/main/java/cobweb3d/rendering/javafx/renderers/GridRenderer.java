package cobweb3d.rendering.javafx.renderers;

import cobweb3d.core.environment.BaseEnvironment;
import cobweb3d.rendering.javafx.SimCamera;
import cobwebutil.MaterialColor;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.joml.Vector3f;

import java.util.List;

public class GridRenderer extends Group {
    private static final boolean drawInnerGrid = false;
    public int gridWidth = 0;
    public int gridHeight = 0;
    public int gridDepth = 0;
    private float gridLineThickness = 0.1f;

    public GridRenderer(BaseEnvironment environment) {
        generateGeometry(environment);
    }

    public void generateGeometry(BaseEnvironment environment) {
        if (environment == null || environment.environmentParams == null) return;
        gridWidth = environment.environmentParams.width;
        gridHeight = environment.environmentParams.height;
        gridDepth = environment.environmentParams.depth;
        gridLineThickness = calculateGridLineThickness();
        List<Node> children = getChildren();
        children.clear();

        AmbientLight light = new AmbientLight(MaterialColor.grey_500.asJFXColor());
        light.getScope().add(this);
        children.add(light);

        for (int x = 0; x < gridWidth + 1; x++) {
            for (int z = 0; z < gridDepth + 1; z++) {
                if (x == 0 || x == gridWidth || z == 0 || z == gridDepth) {
                    children.add(createConnectionRect(new Point3D(x, 0, z), new Point3D(x, gridHeight, z)));
                } else if (drawInnerGrid) {
                    children.add(createConnectionRect(new Point3D(x, 0, z), new Point3D(x, gridHeight, z)));
                }
            }
        }
        for (int y = 0; y < gridHeight + 1; y++) {
            for (int z = 0; z < gridDepth + 1; z++) {
                if (y == 0 || y == gridHeight || z == 0 || z == gridDepth) {
                    children.add(createConnectionRect(new Point3D(0, y, z), new Point3D(gridWidth, y, z)));
                } else if (drawInnerGrid) {
                    children.add(createConnectionRect(new Point3D(0, y, z), new Point3D(gridWidth, y, z)));
                }
            }
        }
        for (int x = 0; x < gridWidth + 1; x++) {
            for (int y = 0; y < gridHeight + 1; y++) {
                if (y == 0 || y == gridHeight || x == 0 || x == gridWidth) {
                    children.add(createConnectionRect(new Point3D(x, y, 0), new Point3D(x, y, gridDepth)));
                } else if (drawInnerGrid) {
                    children.add(createConnectionRect(new Point3D(x, y, 0), new Point3D(x, y, gridDepth)));
                }
            }
        }
    }

    public Box createConnectionRect(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Box line = new Box(gridLineThickness, height, gridLineThickness);
        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
        return line;
    }

    private float calculateGridLineThickness() {
        return Math.max(0.01f, 0.01f * (Math.max(Math.max((gridWidth / 25), (gridHeight / 25)), (gridDepth / 50))));
    }

    public void focusCamera(SimCamera camera) {
        Vector3f gridCenter = new Vector3f(((float) gridWidth) / 2f, ((float) gridHeight) / 2f,
                ((float) gridDepth) / 2f);
        float gridBoundingRadius = new Vector3f(gridWidth, gridHeight,
                gridDepth).length() / 2;
        float optimalDistanceY = ((float) gridHeight / 2) / (float) Math.tan(camera.fovYr() / 2);
        float optimalDistanceX = ((float) gridWidth / 2) / (float) Math.tan(camera.fovXr() / 2);
        camera.lookAt(new Vector3f(gridCenter.x, gridCenter.y, -(Math.max(optimalDistanceY, optimalDistanceX)) * 1.05f),
                new Vector3f(gridCenter.x, gridCenter.y, gridCenter.z),
                new Vector3f(0, 1, 0));
    }
}
