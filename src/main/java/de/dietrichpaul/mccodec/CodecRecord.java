package de.dietrichpaul.mccodec;

import com.google.gson.JsonElement;

import java.util.Optional;

/**
 * An implementation of the {@link Codec} interface using a Java record.
 * This record stores all the necessary components for encoding, decoding, and describing a DTO.
 *
 * @param <I>          input type (e.g., DataInput)
 * @param <O>          output type (e.g., DataOutput)
 * @param <T>          the object type
 * @param name         the codec name
 * @param description  an optional description of the codec
 * @param format       the format type of the codec (e.g., POJO, SINGLETON)
 * @param attributes   a JSON representation of the attributes, or null
 * @param type         the class type of the object T
 * @param serializer   the serialization logic
 * @param deserializer the deserialization logic
 * @param copyFunction the cloning logic
 */
public record CodecRecord<I, O, T>(
        String name,
        Optional<String> description,
        CodecFormat format,
        JsonElement attributes,
        Class<T> type,
        Serializer<O, T> serializer,
        Deserializer<I, T> deserializer,
        CopyFunction<T> copyFunction
) implements Codec<I, O, T> {
}