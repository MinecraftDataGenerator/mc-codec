package de.dietrichpaul.mccodec;

/**
 * Functional interface for creating a new instance of an object using its default constructor.
 * Used by POJO codecs to create a blank object for deserialization.
 *
 * @param <T> the type of object to create
 */
public interface Factory<T> {

    /**
     * Creates a new instance of the object T.
     *
     * @return A new instance of T.
     */
    T create();
}