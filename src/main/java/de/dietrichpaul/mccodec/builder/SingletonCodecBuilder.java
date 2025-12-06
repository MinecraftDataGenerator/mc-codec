package de.dietrichpaul.mccodec.builder;

import de.dietrichpaul.mccodec.*;

import java.util.Objects;
import java.util.Optional;

/**
 * Builds a singleton-style {@link Codec} for handling primitive types, Strings,
 * or other simple data structures within DTO-like objects.
 *
 * <p>This builder allows you to construct a {@link Codec} that always works with
 * a single instance of {@code T}. The optional {@link CopyFunction} can be used
 * to create defensive copies of the object before encoding or decoding.
 * If the {@code CopyFunction} is not set (thus set to {@code null}), the object will be passed through
 * as-is without cloning.
 *
 * <p>Typical use cases include codecs for:
 * <ul>
 *     <li>Primitive wrapper types</li>
 *     <li>Strings</li>
 *     <li>Lightweight DTOs or POJOs that only have one attribute</li>
 * </ul>
 *
 * @param <I> the input type used during deserialization (e.g. {@code DataInput})
 * @param <O> the output type used during serialization (e.g. {@code DataOutput})
 * @param <T> the value or DTO type being serialized/deserialized
 */
public class SingletonCodecBuilder<I, O, T> implements Builder<Codec<I, O, T>> {

    private String name;
    private String description;

    private Class<T> type;
    private Deserializer<I, T> deserializer;
    private Serializer<O, T> serializer;
    private CopyFunction<T> copyFunction;

    /**
     * Sets the name of this codec.
     *
     * @param name the codec name
     * @return this builder for chaining
     */
    public SingletonCodecBuilder<I, O, T> setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns the codec name.
     *
     * @return the name of the codec, or null if not set
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the description of this codec.
     *
     * @param description description text
     * @return this builder for chaining
     */
    public SingletonCodecBuilder<I, O, T> setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the codec description.
     *
     * @return the description, or null if not set
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the runtime type of {@code T} that this codec handles.
     * This is optional but useful for type validation or reflective codecs.
     *
     * @param type the class representing {@code T}
     * @return this builder instance
     */
    public SingletonCodecBuilder<I, O, T> setType(Class<T> type) {
        this.type = type;
        return this;
    }

    /**
     * Returns the configured type token for {@code T}.
     *
     * @return the class representing {@code T}, or {@code null} if not set
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Sets the {@link CopyFunction} used to clone or defensively copy values
     * of type {@code T}. If this value is {@code null}, the codec will simply
     * return the original value without cloning.
     *
     * @param copyFunction the copy function to use, or {@code null}
     * @return this builder instance for chaining
     */
    public SingletonCodecBuilder<I, O, T> setCopyFunction(CopyFunction<T> copyFunction) {
        this.copyFunction = copyFunction;
        return this;
    }

    /**
     * Returns the configured {@link CopyFunction}, or {@code null} if none was set.
     *
     * @return the copy function or {@code null}
     */
    public CopyFunction<T> getCopyFunction() {
        return copyFunction;
    }

    /**
     * Sets the {@link Serializer} used to write values of type {@code T}.
     *
     * @param serializer the serializer to use
     * @return this builder instance for chaining
     */
    public SingletonCodecBuilder<I, O, T> setSerializer(Serializer<O, T> serializer) {
        this.serializer = serializer;
        return this;
    }

    /**
     * Sets the {@link Deserializer} used to read values of type {@code T}.
     *
     * @param deserializer the deserializer to use
     * @return this builder instance for chaining
     */
    public SingletonCodecBuilder<I, O, T> setDeserializer(Deserializer<I, T> deserializer) {
        this.deserializer = deserializer;
        return this;
    }

    /**
     * Returns the configured {@link Serializer}.
     *
     * @return the serializer, or {@code null} if none was set
     */
    public Serializer<O, T> getSerializer() {
        return serializer;
    }

    /**
     * Returns the configured {@link Deserializer}.
     *
     * @return the deserializer, or {@code null} if none was set
     */
    public Deserializer<I, T> getDeserializer() {
        return deserializer;
    }

    /**
     * Creates a new {@link Codec} instance using the configured serializer,
     * deserializer, and optional copy function.
     *
     * <p>The {@link CopyFunction} defaults to a pass-through function
     * {@code t -> t} if not provided.
     *
     * @return a fully constructed codec
     * @throws NullPointerException if the serializer or deserializer is missing
     */
    @Override
    public Codec<I, O, T> create() {
        Serializer<O, T> serializer = getSerializer();
        Deserializer<I, T> deserializer = getDeserializer();
        CopyFunction<T> copyFunction =
                Objects.requireNonNullElse(getCopyFunction(), t -> t);

        return new CodecRecord<>(
                name,
                Optional.ofNullable(description),
                CodecFormat.SINGLETON,
                null,
                Objects.requireNonNull(type, "type"),
                Objects.requireNonNull(serializer, "serializer"),
                Objects.requireNonNull(deserializer, "deserializer"),
                copyFunction
        );
    }
}
