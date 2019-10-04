package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A resource.
 */
public interface Resource {

    /**
     * Gets the unique key of the resource.
     *
     * @return The unique resource key.
     */
    ResourceKey getKey();

    @Override boolean equals(@Nullable Object other);

    @Override int hashCode();

    @Override String toString();

}
