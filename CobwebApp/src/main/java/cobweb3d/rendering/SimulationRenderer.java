package cobweb3d.rendering;

import cobweb3d.impl.Simulation;
import cobweb3d.ui.UpdatableUI;

import java.awt.*;

public interface SimulationRenderer extends UpdatableUI {

    void refreshSimulation();
    void bindSimulation(Simulation simulation);
    Component getBackbuffer();

    default SimulationRendererMenuItem getMenuItem() {
        return null;
    }
}
