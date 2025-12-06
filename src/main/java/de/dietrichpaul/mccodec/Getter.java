package de.dietrichpaul.mccodec;

/**
 * Functional interface representing a function that retrieves a value (an attribute)
 * from an owning object (a POJO).
 *
 * @param <V> the type of the value being retrieved (the attribute type)
 * @param <O> the type of the owning object (the POJO type)
 */
@FunctionalInterface
public interface Getter<V, O> {
    /**
     * Retrieves the value from the object.
     *
     * @param object the owning object.
     * @return The retrieved value V.
     */
    V get(O object);
}