package cobweb3d.ui.swing.config;

import javax.swing.*;

public interface ConfigPage {
    JPanel getPanel();

    void validateUI() throws IllegalArgumentException;
}