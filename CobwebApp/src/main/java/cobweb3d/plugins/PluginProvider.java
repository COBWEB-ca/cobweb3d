package cobweb3d.plugins;

import cobweb3d.impl.Simulation;
import cobweb3d.impl.SimulationConfig;
import cobweb3d.plugins.mutators.AgentMutator;
import cobweb3d.plugins.mutators.ConfiguratedMutator;
import cobweb3d.plugins.mutators.DataLoggingMutator;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PluginProvider {

    public static Set<Class<? extends AgentMutator>> getAvailablePlugins() {
        Reflections pluginsPackage = new Reflections("cobweb3d.plugins");
        SortedSet<Class<? extends AgentMutator>> orderedClasses = new TreeSet<>(new PluginOrderComparator());
        orderedClasses.addAll(pluginsPackage.getSubTypesOf(AgentMutator.class).stream().filter(p -> !p.isInterface() && !Modifier.isAbstract(p.getModifiers())).collect(Collectors.toList()));
        return orderedClasses;
    }

    public static Set<Class<? extends DataLoggingMutator>> getLoggingPlugins() {
        Reflections pluginsPackage = new Reflections("cobweb3d.plugins");
        SortedSet<Class<? extends DataLoggingMutator>> orderedClasses = new TreeSet<>(new PluginOrderComparator());
        orderedClasses.addAll(pluginsPackage.getSubTypesOf(DataLoggingMutator.class).stream().filter(p -> !p.isInterface() && !Modifier.isAbstract(p.getModifiers())).collect(Collectors.toList()));
        return orderedClasses;
    }

    /**
     * This method uses reflection to detect which plugins are loaded
     * into the program in real time, then returns all of them in a set.
     * @return A set which includes all the plugins that is in use.
     */
    public static Set<Class<? extends AgentMutator>> getConfiguratedPlugins() {
        Reflections pluginsPackage = new Reflections("cobweb3d.plugins");
        SortedSet<Class<? extends AgentMutator>> orderedClasses = new TreeSet<>(new PluginOrderComparator());
        orderedClasses.addAll(pluginsPackage
                .getSubTypesOf(AgentMutator.class).stream()
                .filter(p -> !p.isInterface() && ConfiguratedMutator.class.isAssignableFrom(p) && !Modifier.isAbstract(p.getModifiers())).collect(Collectors.toList()));
        return orderedClasses;
    }


    public static void loadPluginConfigs(Simulation simulation, SimulationConfig simulationConfig) {
        for (AgentMutator agentMutator : simulation.mutatorListener.getAllMutators()) {
            try {
                if (ConfiguratedMutator.class.isAssignableFrom(agentMutator.getClass())) {
                    Field[] fields = simulationConfig.getClass().getDeclaredFields();
                    for (Field f : fields) {
                        if (((ConfiguratedMutator<?>) agentMutator).acceptsParam(f.getType())) {
                            ((ConfiguratedMutator) agentMutator).setParams(simulation, f.get(simulationConfig), simulationConfig.getAgentTypes());
                            break;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * This method gets all the plugins which are currently in use, then creates
     * the corresponding mutator instances and adds them into "simulation".
     *
     * @param simulation the current simulation.
     */
    public static void constructPlugins(Simulation simulation) {
        simulation.mutatorListener.clearMutators();
        Set<Class<? extends AgentMutator>> classes = getConfiguratedPlugins();
        for (Class<? extends AgentMutator> plugin : classes) {
            try {
                AgentMutator mutator = plugin.newInstance();
                simulation.mutatorListener.addMutator(mutator);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class PluginOrderComparator implements Comparator<Class<?>> {
        @Override
        public int compare(Class<?> o1, Class<?> o2) {
            return o1.getSimpleName().compareTo(o2.getSimpleName());
        }
    }
}
