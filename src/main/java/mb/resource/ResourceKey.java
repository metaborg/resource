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


    @Override boolean equals(@Nullable Object other);

    @Override int hashCode();

    @Override String toString();
}
