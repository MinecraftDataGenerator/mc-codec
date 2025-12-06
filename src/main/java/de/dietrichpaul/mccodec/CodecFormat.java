package de.dietrichpaul.mccodec;

/**
 * Defines the declarative format types of a {@link Codec}.
 */
public enum CodecFormat {

    /**
     * Codec for primitive types, Strings, or single attributes.
     */
    SINGLETON("singleton"),
    /**
     * Codec for mutable POJOs with getters and setters.
     */
    MUTABLE_POJO("mutable_pojo"),
    /**
     * Codec for immutable objects typically using a builder pattern.
     */
    IMMUTABLE_POJO("immutable_pojo");

    private final String name;

    CodecFormat(String name) {
        this.name = name;
    }

    /**
     * Returns the programmatic name of the codec format (e.g., "mutable_pojo").
     *
     * @return The format name.
     */
    public String getName() {
        return name;
    }
}