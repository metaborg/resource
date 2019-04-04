package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;

public interface ResourceKey extends Serializable {
    Serializable qualifier();

    Serializable id();


    @Override boolean equals(@Nullable Object other);

    @Override int hashCode();

    @Override String toString();
}
