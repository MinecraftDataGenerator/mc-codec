package de.dietrichpaul.mccodec;

import de.dietrichpaul.mccodec.builder.BaseAttribute;
import de.dietrichpaul.mccodec.builder.ImmutablePOJOCodecBuilder;
import de.dietrichpaul.mccodec.builder.MutablePOJOCodecBuilder;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a single attribute within a complex object (POJO) that is handled
 * by a {@link MutablePOJOCodecBuilder} or {@link ImmutablePOJOCodecBuilder}.
 * It links the attribute's name, its access methods, and its specific codec.
 *
 * @param <I>         input type (e.g., DataInput)
 * @param <O>         output type (e.g., DataOutput)
 * @param <A>         the attribute's data type (the value type)
 * @param <T>         the object type that holds this attribute (the container type)
 * @param name        the programmatic name of the attribute
 * @param description an optional description of the attribute
 * @param getter      a function to retrieve the attribute value
 * @param setter      a function to set the attribute value
 * @param codec       the specific codec for the attribute's data type
 */
public record Attribute<I, O, A, T>(
        String name,
        Optional<String> description,
        Getter<A, T> getter,
        Setter<A, T> setter,
        Codec<I, O, A> codec
)  implements BaseAttribute<I,O> {

    /**
     * Canonical constructor with non-null checks for mandatory fields.
     */
    public Attribute {
        Objects.requireNonNull(name, "Attribute name cannot be null");
        Objects.requireNonNull(getter, "Getter cannot be null");
        Objects.requireNonNull(setter, "Setter cannot be null");
        Objects.requireNonNull(codec, "Codec cannot be null");
    }

    /**
     * Returns a string representation in the format: {@code name ∈ codec}.
     *
     * @return Formatted string representation of the attribute.
     */
    @Override
    public String toString() {
        return name + " ∈ " + codec.toString();
    }
}