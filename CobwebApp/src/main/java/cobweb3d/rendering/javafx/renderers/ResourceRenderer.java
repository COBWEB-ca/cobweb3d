package cobweb3d.rendering.javafx.renderers;

import cobweb3d.core.location.Direction;
import cobweb3d.core.location.Location;
import cobwebutil.MaterialColor;
import cobwebutil.math.TransformUtil;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Scale;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;

public class ResourceRenderer extends Group {
    private Map<Integer, Color> typeColorMap;
    private Map<Integer, Material> typeMaterialMap;
    private ToonRenderer toonRenderer;
    private OutlineRenderer outRenderer;

    private boolean doToonShading = true;
    private boolean doOutlineRendering = true;

    public ResourceRenderer() {
        this(true, true);
    }
    public ResourceRenderer(boolean toonShading, boolean outlineRendering) {
        super();
        setCache(false);

        typeColorMap = new HashMap<>();
        typeMaterialMap = new HashMap<>();

        AmbientLight ambientLight = new AmbientLight(MaterialColor.green_50.asJFXColor());
        ambientLight.getScope().add(this);
        PointLight pointLight = new PointLight(MaterialColor.green_50.asJFXColor());
        pointLight.getScope().add(this);
        getChildren().addAll(ambientLight, pointLight);

        if ((doToonShading = toonShading)) toonRenderer = new ToonRenderer();
        if ((outlineRendering = outlineRendering)) outRenderer = new OutlineRenderer();
    }

    public void drawFood(Location l, int foodType) {
        
    }

    private class ToonRenderer {
        private PhongMaterial toonMaterial;

        public ToonRenderer() {
            toonMaterial = new PhongMaterial(MaterialColor.black_1000.asJFXColor());
        }

        public void drawFood(Location location, int foodType, Vector3f temp) {
            Sphere toonMesh = new Sphere(0.29f);

            toonMesh.setMaterial(toonMaterial);
            toonMesh.setCullFace(CullFace.FRONT);
            Vector3i i = new Vector3i();
            Vector3f f = new Vector3f();
            // Spheres have no orientation so we just use a blank vector for <direction> and <up>
            TransformUtil.TransformNode(toonMesh, location, 0.5f, 0.5f, 0.5f, i, f);
            toonMesh.getTransforms().add(new Scale(1.05, 1.05, 1.05, 0.5, 0.5, 0.5));
            getChildren().add(toonMesh);
        }
    }

    private class OutlineRenderer {
        private PhongMaterial outlineMaterial;

        public OutlineRenderer() {
            outlineMaterial = new PhongMaterial(MaterialColor.black_1000.asJFXColor());
        }

        public void drawFood(Location location, int foodType, Vector3f temp) {
            Sphere outlineMesh = new Sphere(0.3f);
            outlineMesh.setMaterial(outlineMaterial);
            outlineMesh.setCullFace(CullFace.NONE);
            outlineMesh.setDrawMode(DrawMode.LINE);

            Vector3i i = new Vector3i();
            Vector3f f = new Vector3f();
            TransformUtil.TransformNode(outlineMesh, location, 0.5f, 0.5f, 0.5f, i, f);
            getChildren().add(outlineMesh);

        }
    }
}
