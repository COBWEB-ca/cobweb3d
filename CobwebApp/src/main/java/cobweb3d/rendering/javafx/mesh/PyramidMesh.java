package cobweb3d.rendering.javafx.mesh;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.joml.Vector3f;

public class PyramidMesh extends MeshView {
    public static final Vector3f UP = new Vector3f(0, -1, 0);
    private static final float DEFAULT_HEIGHT = 1f;
    private static final float DEFAULT_HYPOTENUSE = 1f;
    private final FloatProperty height = new SimpleFloatProperty() {
        @Override
        protected void invalidated() {
            setMesh(createPyramid(getSize(), getHeight()));
        }
    };
    /*
       Properties
     */
    private final FloatProperty size = new SimpleFloatProperty() {
        @Override
        protected void invalidated() {
            setMesh(createPyramid(getSize(), getHeight()));
        }
    };

    public PyramidMesh() {
        this(DEFAULT_HEIGHT, DEFAULT_HYPOTENUSE);
    }

    public PyramidMesh(float size, float height) {
        setSize(size);
        setHeight(height);
    }

    private TriangleMesh createPyramid(float size, float height) {
        TriangleMesh mesh = new TriangleMesh();

        float sz = size; // Size
        float hs = sz / 2f; // Half-size
        float he = height; // Height

        mesh.getPoints().addAll(
                hs, 0, hs,    //point O
                0, he, 0,  //point A
                0, he, sz,    //point B
                sz, he, 0,    //point C
                sz, he, sz    //point D
        );

        mesh.getTexCoords().addAll(0, 0);

        mesh.getFaces().addAll(
                0, 0, 2, 0, 1, 0,        // O-B-A
                0, 0, 1, 0, 3, 0,        // O-A-C
                0, 0, 3, 0, 4, 0,        // O-C-D
                0, 0, 4, 0, 2, 0,        // O-D-B
                4, 0, 1, 0, 2, 0,        // D-A-B
                4, 0, 3, 0, 1, 0        // D-C-A
        );

        return mesh;
    }

    public final float getSize() {
        return size.get();
    }

    public final void setSize(float value) {
        size.set(value);
    }

    public FloatProperty sizeProperty() {
        return size;
    }

    public final float getHeight() {
        return height.get();
    }

    public final void setHeight(float value) {
        height.set(value);
    }

    public FloatProperty heightProperty() {
        return height;
    }
}
