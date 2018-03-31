package cobwebutil.io;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Catalog of possible values a field of a given type can have.
 */
public class ChoiceCatalog {

    private Map<Class<? extends ParameterChoice>, Set<? extends ParameterChoice>> catalog =
            new HashMap<Class<? extends ParameterChoice>, Set<? extends ParameterChoice>>();

    @SuppressWarnings("unchecked")
    public <T extends ParameterChoice> void addChoice(Class<T> fieldType, T instance) {
        Set<T> s = (Set<T>) catalog.get(fieldType);
        if (s == null) {
            s = new LinkedHashSet<T>();
            catalog.put(fieldType, s);
        }
        s.add(instance);
    }

    @SuppressWarnings("unchecked")
    public <T extends ParameterChoice> Set<T> getChoices(Class<T> fieldType) {
        Set<T> s = (Set<T>) catalog.get(fieldType);
        return s;
    }

    public static ChoiceCatalog DEFAULT = new ChoiceCatalog();
}
