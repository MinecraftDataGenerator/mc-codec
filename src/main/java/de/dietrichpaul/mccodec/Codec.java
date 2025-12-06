package de.dietrichpaul.mccodec;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

/**
 * Represents a flexible codec system for serialization, deserialization, and cloning
 * of Data Transfer Objects (DTOs) or Plain Old Java Objects (POJOs).
 *
 * @param <I> The input type used during deserialization (e.g., DataInput, JsonObject, ByteBuffer).
 * @param <O> The output type used during serialization (e.g., DataOutput, JsonObject, BufferedWriter).
 * @param <T> The type of the object being encoded/decoded.
 */
public interface Codec<I, O, T> {

    /**
     * Returns the name of the codec, which is useful for declarative representation and debugging.
     *
     * @return The codec's name.
     */
    String name();

    /**
     * Returns an optional description providing context or detailed information about the codec.
     *
     * @return An {@code Optional} containing the description, or an empty {@code Optional} if none is set.
     */
    Optional<String> description();

    /**
     * Returns the runtime class type of the object this codec handles.
     *
     * @return The class type {@code T}.
     */
    Class<T> type();

    /**
     * Returns the serialization function used to convert the object {@code T} into the output format {@code O}.
     *
     * @return The {@link Serializer} instance.
     */
    Serializer<O, T> serializer();

    /**
     * Returns the deserialization function used to convert the input format {@code I} into the object {@code T}.
     *
     * @return The {@link Deserializer} instance.
     */
    Deserializer<I, T> deserializer();

    /**
     * Returns the function used to create a defensive copy or clone of the object {@code T}.
     *
     * @return The {@link CopyFunction} instance.
     */
    CopyFunction<T> copyFunction();

    /**
     * Returns a JSON element representing the attributes or structure of the object defined by this codec.
     * This is typically used for declarative structure visualization.
     *
     * @return A {@link JsonElement} detailing the attributes, or {@code null} if the codec has no attributes (e.g., a singleton).
     */
    JsonElement attributes();

    /**
     * Returns the format of the codec (e.g., SINGLETON, MUTABLE_POJO, IMMUTABLE_POJO).
     *
     * @return The {@link CodecFormat}.
     */
    CodecFormat format();

    /**
     * Generates a complete JSON object dump of the codec's configuration,
     * including name, description, type, format, and attribute structure.
     * It might be possible to reconstruct a Codec using that.
     *
     * @return A {@link JsonObject} representing the full declarative structure of the codec.
     */

    default JsonObject toJsonDump() {
        JsonObject object = new JsonObject();
        object.addProperty("name", name());
        description().ifPresent(d -> object.addProperty("description", d));
        object.addProperty("type", type().getName());
        object.addProperty("format", format().getName());

        JsonElement attributes = attributes();
        if (attributes != null) {
            object.add("attributes", attributes);
        }

        return object;
    }

}
