package cobwebutil.io;

import cobwebutil.MutatableFloat;
import cobwebutil.MutatableInt;

import java.lang.reflect.Field;


public class CloneHelper {

    /**
     * Finds MutatableFloat/Int fields in given ParameterSerializable and replaces them with new
     * instances with the same raw value and no multipliers applied.
     */
    public static void resetMutatable(ParameterSerializable obj) {
        Class<? extends ParameterSerializable> clazz = obj.getClass();
        for (Field f : clazz.getFields()) {
            try {
                if (f.getType().equals(MutatableFloat.class)) {
                    MutatableFloat original = (MutatableFloat) f.get(obj);
                    MutatableFloat copy = new MutatableFloat(original.getRawValue());
                    f.set(obj, copy);

                } else if (f.getType().equals(MutatableInt.class)) {
                    MutatableInt original = (MutatableInt) f.get(obj);
                    MutatableInt copy = new MutatableInt(original.getRawValue());
                    f.set(obj, copy);

                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException("Unexpected reflection error", ex);
            }
        }

    }
}
