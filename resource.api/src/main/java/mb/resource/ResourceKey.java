package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;

/**
 * A resource key, which uniquely identifies a resource.
 */
public interface ResourceKey extends Serializable {
    /**
     * Gets the qualifier of the key.
     *
     * @return Qualifier of the key.
     */
    String getQualifier();

    /**
     * Gets the identifier of the key.
     *
     * @return Identifier of the key.
     */
    Serializable getId();

    /**
     * Gets the identifier of the key as a string.
     *
     * @return Identifier of the key as a string.
     */
    String getIdAsString();


    /**
     * Converts this key to a {@link QualifiedResourceKeyString qualified resource key string}.
     *
     * @return {@link QualifiedResourceKeyString Qualified resource key string} for given identifier.
     */
    default QualifiedResourceKeyString asResourceKeyString() {
        return QualifiedResourceKeyString.of(getQualifier(), getIdAsString());
    }

    /**
     * Converts this key to a string that can be parsed into a {@link QualifiedResourceKeyString Qualified resource key
     * string} with {@link QualifiedResourceKeyString#parse(String)}.
     *
     * @return String that can be parsed into a {@link QualifiedResourceKeyString Qualified resource key string} with
     * {@link QualifiedResourceKeyString#parse(String)}
     */
    default String asString() {
        return QualifiedResourceKeyString.toString(getQualifier(), getIdAsString());
    }


    @Override boolean equals(@Nullable Object other);

    @Override int hashCode();

    /**
     * Gets this key as a string for display/debugging purposes only.
     *
     * @return String representation of this key.
     */
    @Override String toString();
}
