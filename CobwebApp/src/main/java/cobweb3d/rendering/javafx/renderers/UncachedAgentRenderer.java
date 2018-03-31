package cobweb3d.rendering.javafx.renderers;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.agent.Agent;
import cobweb3d.rendering.javafx.mesh.BaselessPyramidMesh;
import cobweb3d.rendering.javafx.mesh.PyramidMesh;
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
import javafx.scene.transform.Scale;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UncachedAgentRenderer extends Group {
    private Map<Integer, Color> typeColorMap;
    private Map<Integer, Material> typeMaterialMap;
    private ToonRenderer toonRenderer;
    private OutlineRenderer outRenderer;

    private boolean doToonShading = true;
    private boolean doOutlineRendering = true;

    public UncachedAgentRenderer() {
        this(true, true);
    }

    public UncachedAgentRenderer(boolean toonShading, boolean outlineRendering) {
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


    public void drawAgents(Collection<BaseAgent> agentList) {
        PyramidMesh pyramidMesh;
        if (agentList != null) {
            Vector3f temp = new Vector3f();
            for (BaseAgent agent : agentList) {
                if (agent == null || !agent.isAlive()) continue;
                pyramidMesh = new PyramidMesh(1, 1);
                if (agent instanceof Agent) {
                    if (!typeMaterialMap.containsKey(agent.getType())) {
                        PhongMaterial material = new PhongMaterial();
                        if (!typeColorMap.containsKey(agent.getType())) {
                            String colorString = ((Agent) agent).params.color;
                            if (colorString != null) {
                                Color color = Color.valueOf(colorString);
                                typeColorMap.put(agent.getType(), color);
                                material.setDiffuseColor(color);
                                material.setSpecularColor(color);
                                material.setSpecularPower(4);
                            }
                        }
                        typeMaterialMap.put(agent.getType(), material);
                    }
                }
                pyramidMesh.setMaterial(typeMaterialMap.get(agent.getType()));
                getChildren().add(pyramidMesh);
                if (agent.position == null || agent.position.direction == null) continue;
                TransformUtil.TransformNode(pyramidMesh, agent.position, 0.5f, 0.5f, 0.5f,
                        agent.position.direction, PyramidMesh.UP, temp);
                if (doOutlineRendering && outRenderer != null) outRenderer.drawAgent(agent, temp);
                if (doToonShading && toonRenderer != null) toonRenderer.drawAgent(agent, temp);
            }
        }
        // TODO: To track movement, render a trail and delay the removal of trail meshes.
    }

    public void clearCache() {
        typeColorMap.clear();
        typeMaterialMap.clear();
    }

    private class ToonRenderer {
        private PhongMaterial toonMaterial;

        public ToonRenderer() {
            toonMaterial = new PhongMaterial(MaterialColor.black_1000.asJFXColor());
        }

        public void drawAgent(BaseAgent agent, Vector3f temp) {
            if (!agent.isAlive()) return;
            PyramidMesh toonMesh = new PyramidMesh(1, 0.99f);

            toonMesh.setMaterial(toonMaterial);
            toonMesh.setCullFace(CullFace.FRONT);

            TransformUtil.TransformNode(toonMesh, agent.position, 0.5f, 0.5f, 0.5f,
                    agent.position.direction, PyramidMesh.UP, temp);
            toonMesh.getTransforms().add(new Scale(1.05, 1.05, 1.05, 0.5, 0.5, 0.5));
            getChildren().add(toonMesh);
        }
    }

    private class OutlineRenderer {
        private PhongMaterial outlineMaterial;

        public OutlineRenderer() {

            outlineMaterial = new PhongMaterial(MaterialColor.black_1000.asJFXColor());
        }

        public void drawAgent(BaseAgent agent, Vector3f temp) {
            if (!agent.isAlive()) return;
            BaselessPyramidMesh outlineMesh = new BaselessPyramidMesh(1, 1);
            outlineMesh.setMaterial(outlineMaterial);
            outlineMesh.setCullFace(CullFace.NONE);
            outlineMesh.setDrawMode(DrawMode.LINE);

            TransformUtil.TransformNode(outlineMesh, agent.position, 0.5f, 0.5f, 0.5f,
                    agent.position.direction, PyramidMesh.UP, temp);
            getChildren().add(outlineMesh);
        }
    }
}