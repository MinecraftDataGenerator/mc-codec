package de.dietrichpaul.mccodec.builder;

import de.dietrichpaul.mccodec.Codec;

import java.util.Optional;

/**
 * Common interface for all attributes, regardless of whether they target a mutable POJO or an immutable Builder.
 * Used by the AbstractPOJOCodecBuilder for list management and by JsonCodecs for structure dumping.
 *
 * @param <I> The input type of the nested codec.
 * @param <O> The output type of the nested codec.
 */
public interface BaseAttribute<I, O> {
    String name();
    Optional<String> description();
    Codec<I, O, ?> codec();
}