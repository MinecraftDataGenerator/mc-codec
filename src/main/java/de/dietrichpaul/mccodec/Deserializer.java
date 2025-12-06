package de.dietrichpaul.mccodec;

import java.io.IOException;

/**
 * Functional interface for deserializing an object from an input source.
 *
 * @param <I> the input source type (e.g., DataInput, JsonObject)
 * @param <T> the object type to be created
 */
@FunctionalInterface
public interface Deserializer<I, T> {

    /**
     * Reads data from the input source and constructs an object of type T.
     *
     * @param input the input source.
     * @return The newly deserialized object of type T.
     * @throws IOException if an I/O error occurs during deserialization.
     */
    T deserialize(I input) throws IOException;
}