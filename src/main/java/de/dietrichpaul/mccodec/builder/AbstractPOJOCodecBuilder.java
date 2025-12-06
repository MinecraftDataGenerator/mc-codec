package de.dietrichpaul.mccodec.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.dietrichpaul.mccodec.Attribute;
import de.dietrichpaul.mccodec.Codec;
import de.dietrichpaul.mccodec.CodecFormat;
import de.dietrichpaul.mccodec.Getter;
import de.dietrichpaul.mccodec.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An abstract base class for builders that construct codecs for complex Plain Old Java Objects (POJOs).
 * <p>
 * This class centralizes the common logic for setting metadata (name, description, type) and
 * managing the list of attributes, regardless of whether the attribute targets a mutable POJO or a builder.
 *
 * @param <I> The input type used during deserialization.
 * @param <O> The output type used during serialization.
 * @param <T> The type of the POJO being encoded/decoded.
 * @param <A> The specific attribute type (e.g., Attribute or ImmutablePojoAttribute).
 * @param <B> The concrete builder type for fluent API chaining (the self-type).
 */
public abstract class AbstractPOJOCodecBuilder<I, O, T, A extends BaseAttribute<I, O>, B extends AbstractPOJOCodecBuilder<I, O, T, A, B>> implements Builder<Codec<I, O, T>> {

    protected String name;
    protected String description;
    protected final Class<T> type;

    /**
     * Internal storage for attributes, typed by the specific attribute record/class A.
     */
    protected final List<A> attributes = new ArrayList<>();

    protected AbstractPOJOCodecBuilder(Class<T> type) {
        this.type = type;
    }

    /**
     * Casts this builder instance to its concrete type for fluent chaining.
     *
     * @return The concrete builder instance.
     */
    @SuppressWarnings("unchecked")
    protected final B self() {
        return (B) this;
    }

    /**
     * Returns an unmodifiable list of all currently configured attributes.
     *
     * @return The list of attributes.
     */
    public List<A> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    /**
     * Finds an attribute by its name.
     *
     * @param name The name of the attribute to find.
     * @return An {@code Optional} containing the attribute if found, otherwise empty.
     */
    public Optional<A> getAttribute(String name) {
        return attributes.stream()
                .filter(attribute -> attribute.name().equals(name))
                .findFirst();
    }

    /**
     * Finds the index of an attribute by its name.
     *
     * @param name The name of the attribute to find the index for.
     * @return The index of the attribute.
     * @throws IllegalArgumentException if no attribute with the specified name is found.
     */
    protected int findAttributeIndex(String name) {
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).name().equals(name)) {
                return i;
            }
        }
        throw new IllegalArgumentException("No attribute with name '" + name + "' found.");
    }


    /**
     * Replaces an existing attribute with a new one based on the attribute's name.
     *
     * @param attribute The new attribute to replace the old one with.
     * @return The builder for chaining.
     * @throws IllegalArgumentException if no attribute with the same name is found.
     */
    public B replace(A attribute) {
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).name().equals(attribute.name())) {
                attributes.set(i, attribute);
                return self();
            }
        }
        throw new IllegalArgumentException("Cannot replace attribute with name '" + attribute.name() + "'. No attribute with that name found.");
    }

    /**
     * Removes an attribute by its name.
     *
     * @param name The name of the attribute to remove.
     * @return The builder for chaining.
     * @throws IllegalArgumentException if no attribute with the specified name is found.
     */
    public B remove(String name) {
        if (!attributes.removeIf(attribute -> attribute.name().equals(name))) {
            throw new IllegalArgumentException("Cannot remove attribute with name '" + name + "'. No attribute with that name found.");
        }
        return self();
    }

    /**
     * Adds an attribute at a specific index. Used internally by addBefore/addAfter.
     *
     * @param index     The index to insert the attribute at.
     * @param attribute The attribute to add.
     */
    protected void addAtIndex(int index, A attribute) {
        attributes.add(index, attribute);
    }


    /**
     * Returns the specific {@link CodecFormat} of the constructed codec.
     *
     * @return The codec format (e.g., MUTABLE_POJO or IMMUTABLE_POJO).
     */
    protected abstract CodecFormat format();

    /**
     * Implements the final logic for creating the codec based on the
     * specific POJO format (mutable or immutable).
     *
     * @return The constructed codec.
     */
    @Override
    public abstract Codec<I, O, T> create();

    /**
     * Creates a JSON array representing the declarative structure of a list of attributes.
     * This is used to generate the {@code attributes()} dump for POJO codecs.
     * <p>
     * It iterates over the attributes, creates a JSON object for each, and recursively
     * calls {@code Codec.toJsonDump()} on the nested codec to show the full hierarchy.
     *
     * @param attributes The list of attributes to document.
     * @param <I>        Input type of the Codec (e.g., JsonObject).
     * @param <O>        Output type of the Codec (e.g., JsonObject).
     * @return A {@code JsonArray} containing the structured representation of all attributes,
     * or {@code null} if the list is empty.
     */
    public static <I, O> JsonElement dumpAttributes(List<? extends BaseAttribute<I, O>> attributes) {
        if (attributes.isEmpty()) {
            return null;
        }

        JsonArray array = new JsonArray();
        for (BaseAttribute<I, O> attribute : attributes) {
            JsonObject attributeObject = new JsonObject();
            attributeObject.addProperty("name", attribute.name());

            // Add description if present
            attribute.description().ifPresent(desc -> attributeObject.addProperty("description", desc));
            // Recursively dump the nested codec's structure
            attributeObject.add("codec", attribute.codec().toJsonDump());
            array.add(attributeObject);
        }
        return array;
    }
}