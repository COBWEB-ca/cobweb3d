package cobwebutil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayUtilities {

    /**
     * Resizes a given array to the given dimensions. If a dimension is already the right size, it is
     * re-used, otherwise a new array is created
     *
     * @param original original array
     * @param newsize  new dimensions of the array
     */
    public static <T> T resizeArray(T original, int... newsize) {
        if (!original.getClass().isArray()) {
            throw new IllegalArgumentException("Cannot resize anything except an array!");
        }

        int originalLen = Array.getLength(original);
        int newLen = newsize[0];
        T result = original;
        Class<?> innerType = original.getClass().getComponentType();

        if (originalLen != newLen) {
            @SuppressWarnings("unchecked")
            T uncheckedResult = (T) Array.newInstance(innerType, newLen);
            result = uncheckedResult;
            System.arraycopy(original, 0, result, 0, Math.min(originalLen, newLen));
        }

        if (innerType.isArray()) {
            for (int i = 0; i < Array.getLength(result); ++i) {
                if (Array.get(result, i) == null) {
                    Array.set(result, i, Array.newInstance(innerType.getComponentType(), newsize[1]));
                }
                int[] otherSizes = new int[newsize.length - 1];
                System.arraycopy(newsize, 1, otherSizes, 0, otherSizes.length);
                Object temp = resizeArray(Array.get(result, i), otherSizes);
                Array.set(result, i, temp);
            }
        }

        return result;
    }

    /**
     * Clones multi-dimensional arrays, the inner-most elements are shallow copies!
     *
     * @param original Array to clone
     * @return Clone of array
     */
    public static <T> T clone(T original) {
        if (!original.getClass().isArray()) {
            throw new IllegalArgumentException("Cannot clone anything except an array!");
        }

        Class<?> innerType = original.getClass().getComponentType();

        int length = Array.getLength(original);
        @SuppressWarnings("unchecked")
        T result = (T) Array.newInstance(innerType, length);
        System.arraycopy(original, 0, result, 0, length);

        if (innerType.isArray()) {
            for (int i = 0; i < length; ++i) {
                Object temp = clone(Array.get(result, i));
                Array.set(result, i, temp);
            }
        }
        return result;
    }

    public static <T> List<T> modifiableList(T[] array) {
        List<T> list = new ArrayList<>(Arrays.asList(array));
        return list;
    }

    public static <T> Iterable<T> nullGuard(Iterable<T> iterable) {
        return iterable == null ? Collections.emptyList() : iterable;
    }
}
