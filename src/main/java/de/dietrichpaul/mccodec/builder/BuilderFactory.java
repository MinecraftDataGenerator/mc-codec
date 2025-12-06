package de.dietrichpaul.mccodec.builder;

/**
 * Functional interface for creating a {@link Builder} instance from an existing object.
 * This is primarily used by the {@link de.dietrichpaul.mccodec.builder.ImmutablePOJOCodecBuilder}
 * to support the creation of a new, identical object (cloning/copying) or for serialization.
 *
 * @param <T> the final object type
 * @param <B> the builder type
 */
public interface BuilderFactory<T, B extends Builder<T>> {

    /**
     * Creates a builder initialized with the values from an existing object.
     *
     * @param of the existing object.
     * @return A builder pre-populated with the values of {@code of}.
     */
    B createBuilder(T of);
}