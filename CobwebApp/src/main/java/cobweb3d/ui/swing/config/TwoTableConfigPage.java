package cobweb3d.ui.swing.config;

import cobweb3d.core.params.PerTypeParam;
import cobwebutil.io.ChoiceCatalog;
import cobwebutil.io.ParameterSerializable;
import cobwebutil.swing.ColorLookup;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;

/**
 * Config page for PerAgentParams<TperType> parameters that also have global properties
 * @param <Tmain> type of param container
 * @param <TperType> type of per-agent-type param object
 */
public class TwoTableConfigPage<Tmain extends PerTypeParam<TperType>, TperType extends ParameterSerializable> implements ConfigPage {

    private final TableConfigPage<Tmain> mainPage;
    private final TableConfigPage<TperType> perTypePage;
    private final JPanel myPanel;

    public TwoTableConfigPage(Class<Tmain> paramClass, Tmain params, String mainName, ColorLookup colors) {
        this(paramClass, params, mainName, colors, "Value", null);
    }

    public TwoTableConfigPage(Class<Tmain> paramClass, Tmain params, String mainName, ColorLookup colors, String mainColumn, ChoiceCatalog catalog) {
        this( paramClass, params,  mainName,  colors,  mainColumn,  catalog, "Agent Preferences", "Agent");
    }

    public TwoTableConfigPage(Class<Tmain> paramClass, Tmain params, String mainName, ColorLookup colors, String mainColumn, ChoiceCatalog catalog,
                              String perTypeGroupName, String typePrefix) {
        @SuppressWarnings("unchecked")
        Tmain[] mainArray = (Tmain[]) Array.newInstance(paramClass, 1);
        mainArray[0] = params;
        mainPage = new TableConfigPage<>(mainArray, mainName, mainColumn);
        perTypePage = new TableConfigPage<>(params.getPerTypeParams(), perTypeGroupName, typePrefix, colors, catalog);

        JPanel mainPanel = mainPage.getPanel();
        mainPanel.setPreferredSize(new Dimension(200, 200));

        JPanel perTypePanel = perTypePage.getPanel();

        myPanel = new JPanel(new BorderLayout());
        myPanel.add(mainPanel, BorderLayout.NORTH);
        myPanel.add(perTypePanel);
    }

    public void setMainPanelHeight(int height) {
        mainPage.getPanel().setPreferredSize(new Dimension(100, height));
    }

    @Override
    public JPanel getPanel() {
        return myPanel;
    }

    @Override
    public void validateUI() throws IllegalArgumentException {
        mainPage.validateUI();
        perTypePage.validateUI();
    }

}