package cobweb3d.rendering.javafx.ui;

import cobweb3d.rendering.SimulationRendererMenuItem;
import cobweb3d.rendering.javafx.FXSimulationRenderer;
import cobwebutil.swing.SimpleAction;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FXSimulationRendererMenuItem implements SimulationRendererMenuItem {

    private FXSimulationRenderer fxSimulationRenderer;
    private List<Component> menuItemList;

    public FXSimulationRendererMenuItem(FXSimulationRenderer simulationRenderer) {
        this.fxSimulationRenderer = simulationRenderer;
    }
    private Action toggleToonshading = new SimpleAction("Toon Shading", e -> {
        boolean selected = ((AbstractButton) e.getSource()).isSelected();
        if (fxSimulationRenderer != null) fxSimulationRenderer.setToonRendering(selected);
    });
    private Action toggleOutlines = new SimpleAction("Outline Rendering", e -> {
        boolean selected = ((AbstractButton) e.getSource()).isSelected();
        if (fxSimulationRenderer != null) fxSimulationRenderer.setOutlineRendering(selected);
    });
    private Action toggleParallelRendering = new SimpleAction("Parallel Rendering", e -> {
        boolean selected = ((AbstractButton) e.getSource()).isSelected();
        if (fxSimulationRenderer != null) fxSimulationRenderer.setParallelRendering(selected);
    });

    private void buildMenuItems() {
        menuItemList = new LinkedList<>();
        JCheckBoxMenuItem toonShadingJMenuItem = new JCheckBoxMenuItem(toggleToonshading);
        toonShadingJMenuItem.setState(fxSimulationRenderer == null || fxSimulationRenderer.toonRendering());
        JCheckBoxMenuItem outlineRenderingJMenuItem = new JCheckBoxMenuItem(toggleOutlines);
        outlineRenderingJMenuItem.setState(fxSimulationRenderer == null || fxSimulationRenderer.outlineRendering());
        JCheckBoxMenuItem parallelRenderingJMenuItem = new JCheckBoxMenuItem(toggleParallelRendering);
        parallelRenderingJMenuItem.setState(fxSimulationRenderer == null || fxSimulationRenderer.parallelRendering());
        menuItemList.add(toonShadingJMenuItem);
        menuItemList.add(outlineRenderingJMenuItem);
        menuItemList.add(new JSeparator());
        menuItemList.add(parallelRenderingJMenuItem);
    }

    @Override
    public Collection<Component> getJMenuItems() {
        if (menuItemList == null) buildMenuItems();
        return menuItemList;
    }
}
