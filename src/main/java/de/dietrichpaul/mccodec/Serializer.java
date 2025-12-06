package de.dietrichpaul.mccodec;

import java.io.IOException;

/**
 * Functional interface for serializing an object to an output sink.
 *
 * @param <O> the output sink type (e.g., DataOutput, JsonObject)
 * @param <T> the object type to be serialized
 */
@FunctionalInterface
public interface Serializer<O, T> {

    /**
     * Writes the object's data to the output sink.
     *
     * @param out    the output sink.
     * @param object the object to serialize.
     * @throws IOException if an I/O error occurs during serialization.
     */
    void serialize(O out, T object) throws IOException;
}