package cobweb3d.rendering.javafx;

import cobweb3d.SimulationRunnerBase;
import cobweb3d.impl.Simulation;
import cobweb3d.rendering.SimulationRenderer;
import cobweb3d.rendering.SimulationRendererMenuItem;
import cobweb3d.rendering.javafx.renderers.GridRenderer;
import cobweb3d.rendering.javafx.renderers.ResourceRenderer;
import cobweb3d.rendering.javafx.renderers.UncachedAgentRenderer;
import cobweb3d.rendering.javafx.ui.FXSimulationRendererMenuItem;
import cobwebutil.MaterialColor;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.JFXPanel;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class FXSimulationRenderer implements SimulationRenderer {
    private JFXPanel jfxPanel;
    private FXSimulationRendererMenuItem fxSimulationRendererMenuItem;

    private Simulation simulation;
    private final Logger logger = Logger.getLogger("FXSimulationRenderer");

    private Parent mainLayout;
    private SimCamera camera;
    private SubScene renderScene;
    private Group rootGroup;
    private GridRenderer gridRenderer;
    private final ChangeListener<Number> resizeListener = (observable, oldValue, newValue) -> resizeRendering((int) renderScene.getParent().getScene().getWidth(),
            (int) renderScene.getParent().getScene().getHeight());
    private UncachedAgentRenderer agentRenderer;
    private ResourceRenderer resourceRenderer;

    private boolean toonRendering = true;
    private boolean outlineRendering = true;
    private boolean parallelRendering = true;
    private boolean running = false;

    AnimationTimer animationTimer;

    public FXSimulationRenderer(SimulationRunnerBase simulationRunner) {
        bindSimulation(simulationRunner.getSimulation());
        simulationRunner.addUIComponent(this);
        initJavaFX();
        fxSimulationRendererMenuItem = new FXSimulationRendererMenuItem(this);
    }

    private void initJavaFX() {
        jfxPanel = new JFXPanel();
        mainLayout = new BorderPane(initRenderScene());
        BorderPane layout = (BorderPane) mainLayout;
        layout.widthProperty().addListener(resizeListener);
        layout.heightProperty().addListener(resizeListener);
        Scene scene = new Scene(layout, MaterialColor.grey_100.asJFXColor());
        jfxPanel.setScene(scene);
        logger.info("Initialized JavaFX");

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawParallel();
            }
        };
        animationTimer.stop();
    }

    private SubScene initRenderScene() {
        gridRenderer = new GridRenderer(simulation.environment);
        agentRenderer = new UncachedAgentRenderer();
        rootGroup = new Group(gridRenderer, agentRenderer);
        camera = new SimCamera();

        renderScene = new SubScene(rootGroup, 200, 200, true, SceneAntialiasing.BALANCED);
        renderScene.setFill(Color.WHITE);
        renderScene.setCamera(camera);

        return renderScene;
    }

    private void focusAndDraw() {
        if (rootGroup != null) {
            if (gridRenderer != null) {
                gridRenderer.generateGeometry(simulation.environment);
                gridRenderer.focusCamera(camera);
            }
            if (agentRenderer != null) agentRenderer.clearCache();
            draw();
        }
    }

    private void draw() {
        if (rootGroup != null) {
            // TODO: FIX FUNCTIONALITY! POOR PERFORMANCE WITH THIS FIX.
            rootGroup.getChildren().remove(agentRenderer);
            agentRenderer = new UncachedAgentRenderer(toonRendering, outlineRendering);
            rootGroup.getChildren().add(agentRenderer);
            if (simulation != null && simulation.environment != null)
                agentRenderer.drawAgents(simulation.environment.getAgents()); // TODO: Check concurrency.
        }
    }

    private void drawParallel() {
        if (rootGroup != null) {
            // TODO: FIX FUNCTIONALITY! POOR PERFORMANCE WITH THIS FIX.
            rootGroup.getChildren().remove(agentRenderer);
            agentRenderer = new UncachedAgentRenderer(toonRendering, outlineRendering);
            rootGroup.getChildren().add(agentRenderer);
            if (simulation != null && simulation.environment != null)
                agentRenderer.drawAgents(new ArrayList<>(simulation.environment.getAgents())); // TODO: Check concurrency.
        }
    }

    @Override
    public void refreshSimulation() {
        if (jfxPanel == null) return;
        animationTimer.stop(); // TODO: Should control the animationTimer?
        Platform.runLater(FXSimulationRenderer.this::focusAndDraw);
    }

    @Override
    public void bindSimulation(Simulation simulation) {
        this.simulation = simulation;
        if (jfxPanel == null) return;
        animationTimer.stop();
        Platform.runLater(FXSimulationRenderer.this::focusAndDraw);
    }

    @Override
    public Component getBackbuffer() {
        return jfxPanel;
    }

    @Override
    synchronized public void update(boolean synchronous) {
        if (jfxPanel == null) return;
        if (!running || !parallelRendering) Platform.runLater(FXSimulationRenderer.this::draw);
    }

    @Override
    public void onStopped() {
        running = false;
        if (animationTimer != null) {
            animationTimer.stop();
            // Fully sync visuals when stopping parallel rendering.
            if (parallelRendering) Platform.runLater(FXSimulationRenderer.this::draw);
        }
    }

    @Override
    public void onStarted() {
        running = true;
        if (parallelRendering && animationTimer != null) animationTimer.start();
    }

    private void resizeRendering(int width, int height) {
        renderScene.setWidth(width);
        renderScene.setHeight(height);
        if (height == 0) height = 1;   // prevent divide by zero
        camera.adjustForResolution(width, height);
        gridRenderer.focusCamera(camera);
    }

    public boolean toonRendering() {
        return toonRendering;
    }

    public void setToonRendering(boolean toonRendering) {
        this.toonRendering = toonRendering;
        update(false);
    }

    public boolean outlineRendering() {
        return outlineRendering;
    }

    public void setOutlineRendering(boolean outlineRendering) {
        this.outlineRendering = outlineRendering;
        update(false);
    }

    public boolean parallelRendering() {
        return parallelRendering;
    }

    public void setParallelRendering(boolean parallelRendering) {
        this.parallelRendering = parallelRendering;
        if (animationTimer != null) {
            if (parallelRendering && running) animationTimer.start();
            else animationTimer.stop();
        } else {
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    draw();
                }
            };
            if (running) animationTimer.start();
        }
    }

    @Override
    public SimulationRendererMenuItem getMenuItem() {
        return fxSimulationRendererMenuItem;
    }

    @Override
    public boolean isReadyToUpdate() {
        return true;
    }
}
