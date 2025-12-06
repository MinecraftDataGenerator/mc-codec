package de.dietrichpaul.mccodec;

/**
 * Functional interface for creating a copy or clone of an object.
 * Used for defensive copying or by the cloning mechanism of POJO codecs.
 *
 * @param <T> the type of object to copy
 */
@FunctionalInterface
public interface CopyFunction<T> {

    /**
     * Creates a copy of the given object.
     *
     * @param of the object to copy.
     * @return A new copy of the object.
     */
    T createCopy(T of);
}