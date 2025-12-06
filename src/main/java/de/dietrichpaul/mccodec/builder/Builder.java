package de.dietrichpaul.mccodec.builder;

/**
 * Interface for the Builder pattern, typically used to construct immutable objects.
 *
 * @param <T> the final object type the builder is meant to construct
 */
public interface Builder<T> {

    /**
     * Constructs and returns the final object.
     *
     * @return The fully constructed object of type T.
     */
    T create();

}