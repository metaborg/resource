package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;


/**
 * A resource key, which uniquely identifies a resource.
 */
public interface ResourceKey extends Serializable {

    /**
     * Gets the qualifier of the resource.
     *
     * @return The qualifier of the resource.
     */
    String getQualifier();

    /**
     * Gets the identifier of the resource.
     *
     * @return The identifier of the resource.
     */
    Serializable getId();

    @Override
    boolean equals(@Nullable Object other);

    @Override
    int hashCode();

    @Override
    String toString();

}
