package de.dietrichpaul.mccodec.builder;

import de.dietrichpaul.mccodec.Codec;
import de.dietrichpaul.mccodec.Getter;
import de.dietrichpaul.mccodec.Setter;

import java.util.Objects;
import java.util.Optional;

/**
 * Specialized attribute record for immutable POJOs.
 * It correctly separates the container types: the getter reads from the final POJO (T),
 * while the setter writes to the intermediate Builder (B).
 *
 * @param <I> input type
 * @param <O> output type
 * @param <A> the attribute's data type
 * @param <T> the final immutable object type (used by the getter)
 * @param <B> the builder type (used by the setter)
 */
public record ImmutablePojoAttribute<I, O, A, T, B extends Builder<T>>(
        String name,
        Optional<String> description,
        Getter<A, T> getter, // Reads from POJO T
        Setter<A, B> setter, // Writes to Builder B
        Codec<I, O, A> codec
) implements BaseAttribute<I, O> {

    /** Canonical constructor with non-null checks for mandatory fields. */
    public ImmutablePojoAttribute {
        Objects.requireNonNull(name, "Attribute name cannot be null");
        Objects.requireNonNull(getter, "Getter cannot be null");
        Objects.requireNonNull(setter, "Setter cannot be null");
        Objects.requireNonNull(codec, "Codec cannot be null");
    }
}