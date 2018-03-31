package cobweb3d.plugins.phenotype;

import cobweb3d.core.params.phenotype.NullPhenotype;
import cobweb3d.core.params.phenotype.Phenotype;
import cobweb3d.impl.params.AgentParams;
import cobweb3d.plugins.states.AgentState;
import cobweb3d.ui.config.FieldPropertyAccessor;
import cobweb3d.ui.config.PropertyAccessor;
import cobweb3d.ui.config.SetterPropertyAccessor;
import cobwebutil.MutatableFloat;
import cobwebutil.MutatableInt;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ParameterSerializable;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class PhenotypeIndex {

    public static Set<Phenotype> getPossibleValues() {
        Set<Phenotype> bindables = new LinkedHashSet<Phenotype>();

        // Null phenotype
        addCheckExisting(bindables, new NullPhenotype());

        // ComplexAgentParams phenotypes
        for (PropertyAccessor p : classGetProperties(AgentParams.class)) {
            addCheckExisting(bindables, new BuiltinPhenotype(p));
        }

        // Plugin phenotypes
        Reflections pluginsPackage = new Reflections("cobweb3d.plugins");
        SortedSet<Class<? extends AgentState>> orderedClasses = new TreeSet<>(new PluginOrderComparator());
        orderedClasses.addAll(pluginsPackage.getSubTypesOf(AgentState.class));

        for (Class<? extends AgentState> stateClass : orderedClasses) {
            try {
                PropertyAccessor stateAccessor = getAgentParamsAccessor(stateClass);
                if (stateAccessor == null)
                    continue;

                Class<?> agentParamType = stateAccessor.getType();

                for (PropertyAccessor p : classGetProperties(agentParamType)) {
                    addCheckExisting(bindables, new PluginPhenotype(stateClass, stateAccessor, p));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Unable to configure phenotypes for " + stateClass.getSimpleName(), ex);
            }
        }

        return Collections.unmodifiableSet(bindables);
    }

    private static PropertyAccessor getAgentParamsAccessor(Class<? extends AgentState> stateClass) {
        try {
            Field agentParamField = stateClass.getField("agentParams");
            FieldPropertyAccessor stateAccessor = new FieldPropertyAccessor(agentParamField);
            return stateAccessor;
        } catch (NoSuchFieldException ex) {
            // not a field, try property
        }

        for (Method m : stateClass.getMethods()) {
            if (m.getName().equals("setAgentParams")) {
                SetterPropertyAccessor stateAccessor = new SetterPropertyAccessor(m);
                return stateAccessor;
            }
        }

        return null;
    }

    private static void addCheckExisting(Set<Phenotype> set, Phenotype value) {
        for (Phenotype existing : set) {
            if (existing.getIdentifier().equals(value.getIdentifier()))
                throw new IllegalArgumentException("Phenotype " + value + " has the same identifier as " + existing);
        }
        if (!set.add(value))
            throw new IllegalArgumentException("Found duplicate phenotype: " + value.getIdentifier());
    }

    private static Collection<PropertyAccessor> classGetProperties(Class<?> clazz) {
        return classGetProperties(clazz, null);
    }

    private static Collection<PropertyAccessor> classGetProperties(Class<?> clazz, PropertyAccessor parent) {
        List<PropertyAccessor> res = new ArrayList<>();
        for (Method p : clazz.getMethods()) {
            // setters only
            if (!p.getName().startsWith("set"))
                continue;

            final SetterPropertyAccessor accessor = new SetterPropertyAccessor(parent, p);
            processAccessor(res, accessor);
        }
        for (Field p : clazz.getFields()) {
            final FieldPropertyAccessor accessor = new FieldPropertyAccessor(parent, p);
            processAccessor(res, accessor);
        }
        return res;
    }

    private static void processAccessor(List<PropertyAccessor> accumulator, final PropertyAccessor accessor) {

        if (MutatableFloat.class.isAssignableFrom(accessor.getType()) ||
                MutatableInt.class.isAssignableFrom(accessor.getType())) {
            accumulator.add(accessor);
        } else if (ParameterSerializable.class.isAssignableFrom(accessor.getType()) &&
                accessor.getAnnotationSource().getAnnotation(ConfDisplayName.class) != null) {
            accumulator.addAll(classGetProperties(accessor.getType(), accessor));
        }
    }

    private static class PluginOrderComparator implements Comparator<Class<?>> {
        @Override
        public int compare(Class<?> o1, Class<?> o2) {
            return o1.getSimpleName().compareTo(o2.getSimpleName());
        }
    }
}
