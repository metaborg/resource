package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface Resource extends IORead {
    ResourceKey getKey();


    @Override boolean equals(@Nullable Object other);

    @Override int hashCode();

    @Override String toString();
}
