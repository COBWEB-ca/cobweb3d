package cobweb3d.plugins.exchange.ui;

import cobweb3d.plugins.exchange.ExchangeParams;
import cobweb3d.ui.swing.config.ConfigPage;
import cobweb3d.ui.util.FileDialogUtil;
import cobweb3d.ui.util.SpringUtilities;
import cobwebutil.swing.FileExtFilter;
import cobwebutil.swing.SimpleAction;

import javax.swing.*;

public class ExchangeLogPage extends JPanel implements ConfigPage {

    private ExchangeParams params;
    private SimpleAction setLogAct = new SimpleAction("Set Log", e -> {
        String path = FileDialogUtil.saveFile(SwingUtilities.getWindowAncestor(this), "Log", FileExtFilter.EXCEL_XLSX);
        if (path != null && !path.isEmpty()) params.logPath = path;
        else params.logPath = null;
    });

    public ExchangeLogPage(ExchangeParams params) {
        this.params = params;
        buildPanel();
    }

    public void buildPanel() {
        setLayout(new SpringLayout());
        JLabel pathLabel = new JLabel("Path: ");
        JButton changePath = new JButton(setLogAct);
        add(pathLabel);
        add(changePath);
        SpringUtilities.makeCompactGrid(this, 2, 1, 0, 0, 0, 0, 50, 0);
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void validateUI() throws IllegalArgumentException {

    }
}