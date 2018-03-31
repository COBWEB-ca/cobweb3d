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
import java.util.Iterator;
import java.util.Map;

public class AgentRenderer extends Group {
    private Map<BaseAgent, PyramidMesh> meshCache;
    private Map<Integer, Color> typeColorMap;
    private Map<Integer, Material> typeMaterialMap;
    private ToonRenderer toonRenderer;
    private OutlineRenderer outRenderer;

    public AgentRenderer() {
        super();
        setCache(false);
        meshCache = new HashMap<>();
        typeColorMap = new HashMap<>();
        typeMaterialMap = new HashMap<>();
        AmbientLight ambientLight = new AmbientLight(MaterialColor.green_50.asJFXColor());
        ambientLight.getScope().add(this);
        PointLight pointLight = new PointLight(MaterialColor.green_50.asJFXColor());
        pointLight.getScope().add(this);
        getChildren().addAll(ambientLight, pointLight);
        toonRenderer = new ToonRenderer();
        outRenderer = new OutlineRenderer();
    }

    public void drawAgents(Collection<BaseAgent> agentList) {
        PyramidMesh pyramidMesh;
        if (agentList != null) {
            Iterator<BaseAgent> iter = agentList.iterator();
            while (iter.hasNext()) {
                BaseAgent agent = iter.next();
                if (!agent.isAlive()) {
                    getChildren().remove(meshCache.get(agent));
                    meshCache.remove(agent);
                    toonRenderer.removeAgent(agent);
                    outRenderer.removeAgent(agent);
                    continue;
                }
                if (meshCache.containsKey(agent)) {
                    pyramidMesh = meshCache.get(agent);
                    pyramidMesh.getTransforms().clear();
                } else {
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
                    meshCache.put(agent, pyramidMesh);
                    getChildren().add(pyramidMesh);
                }
                TransformUtil.TransformNode(pyramidMesh, new Vector3f(agent.position), 0.5f, 0.5f, 0.5f,
                        new Vector3f(agent.position.direction), PyramidMesh.UP);
                outRenderer.drawAgent(agent);
                toonRenderer.drawAgent(agent);
            }
        }
        // TODO: To track movement, render a trail and delay the removal of trail meshes.
    }

    public void clearCache() {
        meshCache.clear();
        typeColorMap.clear();
        typeMaterialMap.clear();
        outRenderer.clearCache();
        toonRenderer.clearCache();
    }

    private class ToonRenderer {
        private Map<BaseAgent, PyramidMesh> toonMeshCache;
        private PhongMaterial toonMaterial;

        public ToonRenderer() {
            toonMeshCache = new HashMap<>();
            toonMaterial = new PhongMaterial(MaterialColor.black_1000.asJFXColor());
        }

        public void drawAgent(BaseAgent agent) {
            if (!agent.isAlive()) return;
            PyramidMesh toonMesh;
            if (toonMeshCache.containsKey(agent)) {
                toonMesh = toonMeshCache.get(agent);
                toonMesh.getTransforms().clear();
            } else {
                toonMesh = new PyramidMesh(1, 0.99f);
                toonMeshCache.put(agent, toonMesh);
                getChildren().add(toonMesh);
            }
            toonMesh.setMaterial(toonMaterial);
            toonMesh.setCullFace(CullFace.FRONT);

            TransformUtil.TransformNode(toonMesh, new Vector3f(agent.position), 0.5f, 0.5f, 0.5f,
                    new Vector3f(agent.position.direction), PyramidMesh.UP);
            toonMesh.getTransforms().add(new Scale(1.05, 1.05, 1.05, 0.5, 0.5, 0.5));
        }

        public void removeAgent(BaseAgent agent) {
            if (!agent.isAlive()) {
                getChildren().remove(toonMeshCache.get(agent));
                toonMeshCache.remove(agent);
            }
        }

        public void clearCache() {
            toonMeshCache.clear();
        }
    }

    private class OutlineRenderer {
        private Map<BaseAgent, BaselessPyramidMesh> outlineMeshCache;
        private PhongMaterial outlineMaterial;

        public OutlineRenderer() {
            outlineMeshCache = new HashMap<>();
            outlineMaterial = new PhongMaterial(MaterialColor.black_1000.asJFXColor());
        }

        public void drawAgent(BaseAgent agent) {
            if (!agent.isAlive()) return;

            BaselessPyramidMesh outlineMesh;
            if (outlineMeshCache.containsKey(agent)) {
                outlineMesh = outlineMeshCache.get(agent);
                outlineMesh.getTransforms().clear();
            } else {
                outlineMesh = new BaselessPyramidMesh(1, 1);
                outlineMeshCache.put(agent, outlineMesh);
                getChildren().add(outlineMesh);
            }
            outlineMesh.setMaterial(outlineMaterial);
            outlineMesh.setCullFace(CullFace.NONE);
            outlineMesh.setDrawMode(DrawMode.LINE);

            TransformUtil.TransformNode(outlineMesh, new Vector3f(agent.position), 0.5f, 0.5f, 0.5f,
                    new Vector3f(agent.position.direction), PyramidMesh.UP);
        }

        public void removeAgent(BaseAgent agent) {
            if (!agent.isAlive()) {
                getChildren().remove(outlineMeshCache.get(agent));
                outlineMeshCache.remove(agent);
            }
        }

        public void clearCache() {
            outlineMeshCache.clear();
        }
    }
}
