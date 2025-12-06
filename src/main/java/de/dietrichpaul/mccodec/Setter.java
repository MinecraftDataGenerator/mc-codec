package de.dietrichpaul.mccodec;

/**
 * Functional interface representing a function that sets a value (an attribute)
 * on an owning object (a POJO or a Builder).
 *
 * @param <V> the type of the value to be set (the attribute type)
 * @param <O> the type of the object receiving the value (the POJO or Builder type)
 */
@FunctionalInterface
public interface Setter<V, O> {
    /**
     * Sets the value V on the object O.
     *
     * @param object the owning object.
     * @param value  the value to set.
     */
    void set(O object, V value);
}