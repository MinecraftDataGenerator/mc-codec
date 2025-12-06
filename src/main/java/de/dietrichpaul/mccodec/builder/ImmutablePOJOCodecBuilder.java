package de.dietrichpaul.mccodec.builder;

import com.google.gson.JsonElement;
import de.dietrichpaul.mccodec.*;

import java.util.Objects;
import java.util.Optional;

/**
 * Builds a {@link Codec} for an **immutable** Plain Old Java Object (POJO).
 * Construction is mediated through an intermediate {@link Builder} object of type {@code B}.
 * <p>
 * This builder uses the specialized {@link ImmutablePojoAttribute} to correctly handle the type split:
 * Getter (for serialization) works on the POJO type {@code T}, and Setter (for deserialization)
 * works on the Builder type {@code B}.
 *
 * @param <I> input type
 * @param <O> output type
 * @param <T> the immutable object type
 * @param <B> the specific Builder type for T (must implement {@link Builder})
 */
public class ImmutablePOJOCodecBuilder<I, O, T, B extends Builder<T>>
        extends AbstractPOJOCodecBuilder<I, O, T, ImmutablePojoAttribute<I, O, ?, T, B>, ImmutablePOJOCodecBuilder<I, O, T, B>> {

    private final BuilderFactory<T, B> builderFactory;
    private final Factory<B> factory;

    /**
     * Constructs a new builder for immutable POJOs.
     *
     * @param builderFactory factory to create builders from existing objects
     * @param factory factory to create new builder instances
     * @param type the class type of the immutable object
     */
    public ImmutablePOJOCodecBuilder(BuilderFactory<T, B> builderFactory, Factory<B> factory, Class<T> type) {
        super(type);
        this.builderFactory = builderFactory;
        this.factory = factory;
        Objects.requireNonNull(builderFactory, "BuilderFactory cannot be null");
        Objects.requireNonNull(factory, "Factory cannot be null");
    }

    /**
     * **STATIC FACTORY METHOD**
     * Creates a new builder for immutable POJOs, specifying the input and output types.
     *
     * @param builderFactory factory to create builders from existing objects (T -> B)
     * @param factory factory to create new builder instances (B)
     * @param type the class type of the immutable object (T)
     * @param inputType the class type for the input source (I)
     * @param outputType the class type for the output sink (O)
     * @param <I> input type
     * @param <O> output type
     * @param <T> the immutable object type
     * @param <B> the specific Builder type for T
     * @return a new builder instance
     */
    public static <I, O, T, B extends Builder<T>> ImmutablePOJOCodecBuilder<I, O, T, B> of(BuilderFactory<T, B> builderFactory, Factory<B> factory, Class<T> type, Class<I> inputType, Class<O> outputType) {
        // inputType and outputType are only used by the compiler for type inference (I, O)
        return new ImmutablePOJOCodecBuilder<>(builderFactory, factory, type);
    }

    /**
     * Adds a new attribute to the end of the list.
     *
     * @param name The programmatic name of the attribute.
     * @param codec The codec for the attribute's value type.
     * @param getter The function to retrieve the value from the final POJO (T).
     * @param setter The function to set the value on the intermediate Builder (B).
     * @param <A> The attribute's value type.
     * @return The builder for chaining.
     */
    public <A> ImmutablePOJOCodecBuilder<I, O, T, B> addLast(String name, Codec<I, O, A> codec, Getter<A, T> getter, Setter<A, B> setter) {
        // Uses the specialized attribute record to store Getter<A, T> and Setter<A, B>.
        attributes.add(new ImmutablePojoAttribute<>(name, Optional.empty(), getter, setter, codec));
        return self();
    }

    /**
     * Adds a new attribute to the end of the list using a pre-constructed ImmutablePojoAttribute object.
     *
     * @param attribute The attribute to add.
     * @return The builder for chaining.
     */
    public <A> ImmutablePOJOCodecBuilder<I, O, T, B> addLast(ImmutablePojoAttribute<I, O, A, T, B> attribute) {
        attributes.add(attribute);
        return self();
    }

    /**
     * Adds a new attribute before the attribute with the specified name.
     *
     * @param beforeName The name of the existing attribute to insert the new one before.
     * @param name The programmatic name of the new attribute.
     * @param codec The codec for the attribute's value type.
     * @param getter The function to retrieve the value from the final POJO (T).
     * @param setter The function to set the value on the intermediate Builder (B).
     * @param <A> The attribute's value type.
     * @return The builder for chaining.
     * @throws IllegalArgumentException if no attribute with {@code beforeName} is found.
     */
    public <A> ImmutablePOJOCodecBuilder<I, O, T, B> addBefore(String beforeName, String name, Codec<I, O, A> codec, Getter<A, T> getter, Setter<A, B> setter) {
        int index = findAttributeIndex(beforeName);
        addAtIndex(index, new ImmutablePojoAttribute<>(name, Optional.empty(), getter, setter, codec));
        return self();
    }

    /**
     * Adds a new attribute before the attribute with the specified name using a pre-constructed ImmutablePojoAttribute object.
     *
     * @param beforeName The name of the existing attribute to insert the new one before.
     * @param attribute The attribute to add.
     * @param <A> The attribute's value type.
     * @return The builder for chaining.
     * @throws IllegalArgumentException if no attribute with {@code beforeName} is found.
     */
    public <A> ImmutablePOJOCodecBuilder<I, O, T, B> addBefore(String beforeName, ImmutablePojoAttribute<I, O, A, T, B> attribute) {
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
     * @param getter The function to retrieve the value from the final POJO (T).
     * @param setter The function to set the value on the intermediate Builder (B).
     * @param <A> The attribute's value type.
     * @return The builder for chaining.
     * @throws IllegalArgumentException if no attribute with {@code afterName} is found.
     */
    public <A> ImmutablePOJOCodecBuilder<I, O, T, B> addAfter(String afterName, String name, Codec<I, O, A> codec, Getter<A, T> getter, Setter<A, B> setter) {
        int index = findAttributeIndex(afterName);
        addAtIndex(index + 1, new ImmutablePojoAttribute<>(name, Optional.empty(), getter, setter, codec));
        return self();
    }

    /**
     * Adds a new attribute after the attribute with the specified name using a pre-constructed ImmutablePojoAttribute object.
     *
     * @param afterName The name of the existing attribute to insert the new one after.
     * @param attribute The attribute to add.
     * @param <A> The attribute's value type.
     * @return The builder for chaining.
     * @throws IllegalArgumentException if no attribute with {@code afterName} is found.
     */
    public <A> ImmutablePOJOCodecBuilder<I, O, T, B> addAfter(String afterName, ImmutablePojoAttribute<I, O, A, T, B> attribute) {
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
    public <A> ImmutablePOJOCodecBuilder<I, O, T, B> replace(String name, Codec<I, O, A> codec, Getter<A, T> getter, Setter<A, B> setter) {
        return replace(new ImmutablePojoAttribute<>(name, Optional.empty(), getter, setter, codec));
    }


    @Override
    protected CodecFormat format() {
        return CodecFormat.IMMUTABLE_POJO;
    }

    /**
     * Builds the codec instance using the configured attributes, serializer, deserializer, and copy function.
     *
     * @return the constructed codec
     */
    @Override
    public Codec<I, O, T> create() {
        Objects.requireNonNull(type, "Type cannot be null");
        Serializer<O, T> serializer = (out, object) -> {
            for (ImmutablePojoAttribute<I, O, ?, T, B> attribute : getAttributes()) {
                @SuppressWarnings("unchecked")
                ImmutablePojoAttribute<I, O, Object, T, B> casted = (ImmutablePojoAttribute<I, O, Object, T, B>) attribute;
                Codec<I, O, Object> codec = casted.codec();

                Object value = casted.getter().get(object);
                codec.serializer().serialize(out, value);
            }
        };

        Deserializer<I, T> deserializer = input -> {
            B builder = factory.create();
            for (ImmutablePojoAttribute<I, O, ?, T, B> attribute : getAttributes()) {
                @SuppressWarnings("unchecked")
                ImmutablePojoAttribute<I, O, Object, T, B> casted = (ImmutablePojoAttribute<I, O, Object, T, B>) attribute;
                Codec<I, O, Object> codec = casted.codec();
                Object value = codec.deserializer().deserialize(input);

                casted.setter().set(builder, value);
            }
            return builder.create();
        };
        CopyFunction<T> copyFunction = (original) -> {
            B builder = builderFactory.createBuilder(original);
            return builder.create();
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