package cobweb3d.ui;

import cobweb3d.impl.SimulationConfig;

import java.io.File;

public interface AppContext {

    File openFile(SimulationConfig config, boolean continuation);

    void quitApplication();
}
