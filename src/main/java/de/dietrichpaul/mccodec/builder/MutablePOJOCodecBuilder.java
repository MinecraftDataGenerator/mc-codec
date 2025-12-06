package de.dietrichpaul.mccodec.builder;

import com.google.gson.JsonElement;
import de.dietrichpaul.mccodec.*;

import java.util.Objects;
import java.util.Optional;

/**
 * Builds a {@link Codec} for a **mutable** Plain Old Java Object (POJO).
 * The target object must have a default (no-argument) constructor and
 * provide appropriate getters and setters for all attributes to be encoded/decoded.
 * <p>
 * It uses the standard {@link Attribute} record where the POJO type {@code T} is the container
 * for both the getter and the setter.
 *
 * @param <I> input type (e.g., DataInput, ByteBuffer)
 * @param <O> output type (e.g., DataOutput, BufferedWriter)
 * @param <T> the type of object to build or serialize
 */
public class MutablePOJOCodecBuilder<I, O, T>
        extends AbstractPOJOCodecBuilder<I, O, T, Attribute<I, O, ?, T>, MutablePOJOCodecBuilder<I, O, T>> {

    private final Factory<T> factory;

    /**
     * Constructs a new builder for mutable POJOs.
     *
     * @param factory the factory to create new instances of {@code T} (default constructor reference)
     * @param type    the class type of {@code T}
     */
    public MutablePOJOCodecBuilder(Factory<T> factory, Class<T> type) {
        super(type);
        this.factory = factory;
        Objects.requireNonNull(factory, "Factory cannot be null");
    }

    /**
     * **STATIC FACTORY METHOD**
     * Creates a new builder for mutable POJOs, specifying the input and output types.
     * This is the preferred way to start building a codec.
     *
     * @param factory    the factory to create new instances of {@code T} (default constructor reference)
     * @param type       the class type of {@code T}
     * @param inputType  the class type for the input source (I)
     * @param outputType the class type for the output sink (O)
     * @param <I>        input type (e.g., DataInput, JsonObject)
     * @param <O>        output type (e.g., DataOutput, JsonObject)
     * @param <T>        the type of object to build or serialize
     * @return a new builder instance
     */
    public static <I, O, T> MutablePOJOCodecBuilder<I, O, T> of(Factory<T> factory, Class<T> type, Class<I> inputType, Class<O> outputType) {
        // inputType and outputType are only used by the compiler for type inference (I, O)
        return new MutablePOJOCodecBuilder<>(factory, type);
    }

    /**
     * Adds a new attribute to the end of the list.
     *
     * @param name   The programmatic name of the attribute.
     * @param codec  The codec for the attribute's value type.
     * @param getter The function to retrieve the value from the POJO.
     * @param setter The function to set the value on the POJO.
     * @param <A>    The attribute's value type.
     * @return The builder for chaining.
     */
    public <A> MutablePOJOCodecBuilder<I, O, T> addLast(String name, Codec<I, O, A> codec, Getter<A, T> getter, Setter<A, T> setter) {
        attributes.add(new Attribute<>(name, Optional.empty(), getter, setter, codec));
        return self();
    }
    
    /**
     * Adds a new attribute to the end of the list using a pre-constructed Attribute object.
     *
     * @param attribute The attribute to add.
     * @return The builder for chaining.
     */
    public <A> MutablePOJOCodecBuilder<I, O, T> addLast(Attribute<I, O, A, T> attribute) {
        attributes.add(attribute);
        return self();
    }

    /**
     * Adds a new attribute before the attribute with the specified name.
     *
     * @param beforeName The name of the existing attribute to insert the new one before.
     * @param name The programmatic name of the new attribute.
     * @param codec The codec for the attribute's value type.
     * @param getter The function to retrieve the value from the POJO.
     * @param setter The function to set the value on the POJO.
     * @param <A> The attribute's value type.
     * @return The builder for chaining.
     * @throws IllegalArgumentException if no attribute with {@code beforeName} is found.
     */
    public <A> MutablePOJOCodecBuilder<I, O, T> addBefore(String beforeName, String name, Codec<I, O, A> codec, Getter<A, T> getter, Setter<A, T> setter) {
        int index = findAttributeIndex(beforeName);
        addAtIndex(index, new Attribute<>(name, Optional.empty(), getter, setter, codec));
        return self();
    }

    /**
     * Adds a new attribute before the attribute with the specified name using a pre-constructed Attribute object.
     *
     * @param beforeName The name of the existing attribute to insert the new one before.
     * @param attribute The attribute to add.
     * @param <A> The attribute's value type.
     * @return The builder for chaining.
     * @throws IllegalArgumentException if no attribute with {@code beforeName} is found.
     */
    public <A> MutablePOJOCodecBuilder<I, O, T> addBefore(String beforeName, Attribute<I, O, A, T> attribute) {
        int index = findAttributeIndex(beforeName);
        addAtIndex(index, attribute);
        return self();
    }

    /**
     * Adds a new attribute after the attribute with the specified name.
     *
     * @param afterName The name of the existing attribute to insert the new one after.
     * @param name The programmatic name of the new attribute.
     * @param codec The codec for the attribute's value type.
     * @param getter The function to retrieve the value from the POJO.
     * @param setter The function to set the value on the POJO.
     * @param <A> The attribute's value type.
     * @return The builder for chaining.
     * @throws IllegalArgumentException if no attribute with {@code afterName} is found.
     */
    public <A> MutablePOJOCodecBuilder<I, O, T> addAfter(String afterName, String name, Codec<I, O, A> codec, Getter<A, T> getter, Setter<A, T> setter) {
        int index = findAttributeIndex(afterName);
        addAtIndex(index + 1, new Attribute<>(name, Optional.empty(), getter, setter, codec));
        return self();
    }

    /**
     * Adds a new attribute after the attribute with the specified name using a pre-constructed Attribute object.
     *
     * @param afterName The name of the existing attribute to insert the new one after.
     * @param attribute The attribute to add.
     * @param <A> The attribute's value type.
     * @return The builder for chaining.
     * @throws IllegalArgumentException if no attribute with {@code afterName} is found.
     */
    public <A> MutablePOJOCodecBuilder<I, O, T> addAfter(String afterName, Attribute<I, O, A, T> attribute) {
        int index = findAttributeIndex(afterName);
        addAtIndex(index + 1, attribute);
        return self();
    }

    /**
     * Replaces an existing attribute with new components. This is an overload of the base method.
     *
     * @param name The programmatic name of the attribute to replace.
     * @param codec The new codec for the attribute's value type.
     * @param getter The new getter function.
     * @param setter The new setter function.
     * @param <A> The attribute's value type.
     * @return The builder for chaining.
     * @throws IllegalArgumentException if no attribute with the same name is found.
     */
    public <A> MutablePOJOCodecBuilder<I, O, T> replace(String name, Codec<I, O, A> codec, Getter<A, T> getter, Setter<A, T> setter) {
        return replace(new Attribute<>(name, Optional.empty(), getter, setter, codec));
    }
    
    @Override
    protected CodecFormat format() {
        return CodecFormat.MUTABLE_POJO;
    }

    /**
     * Builds the final {@link Codec} instance using the configured attributes.
     *
     * @return the constructed codec
     */
    @Override
    public Codec<I, O, T> create() {
        Objects.requireNonNull(type, "Type cannot be null");
        Serializer<O, T> serializer = (out, object) -> {
            for (Attribute<I, O, ?, T> attribute : getAttributes()) {
                @SuppressWarnings("unchecked")
                Attribute<I, O, Object, T> casted = (Attribute<I, O, Object, T>) attribute;
                Codec<I, O, Object> codec = casted.codec();
                // Getter is Getter<Object, T>, object is T. Correct.
                Object value = casted.getter().get(object);
                codec.serializer().serialize(out, value);
            }
        };
        Deserializer<I, T> deserializer = input -> {
            T object = factory.create();
            for (Attribute<I, O, ?, T> attribute : getAttributes()) {
                @SuppressWarnings("unchecked")
                Attribute<I, O, Object, T> casted = (Attribute<I, O, Object, T>) attribute;
                Codec<I, O, Object> codec = casted.codec();
                Object value = codec.deserializer().deserialize(input);
                casted.setter().set(object, value);
            }
            return object;
        };
        @SuppressWarnings("unchecked")
        CopyFunction<T> copyFunction = (original) -> {
            T copy = factory.create();
            for (Attribute<I, O, ?, T> attribute : attributes) {
                Object value = attribute.getter().get(original);
                ((Setter<Object, T>) attribute.setter()).set(copy, value);
            }
            return copy;
        };
        JsonElement attributesDump = dumpAttributes(getAttributes());

        return new CodecRecord<>(
                name,
                Optional.ofNullable(description),
                format(),
                attributesDump,
                type,
                serializer,
                deserializer,
                copyFunction
        );
    }
}