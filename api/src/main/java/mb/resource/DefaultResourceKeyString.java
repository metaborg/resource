package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public class DefaultResourceKeyString implements ResourceKeyString {
    private final @Nullable String qualifier;
    private final String id;

    public DefaultResourceKeyString(@Nullable String qualifier, String id) {
        this.qualifier = qualifier;
        this.id = id;
    }

    public DefaultResourceKeyString(String id) {
        this(null, id);
    }

    @Override public @Nullable String getQualifier() {
        return qualifier;
    }

    @Override public String getId() {
        return id;
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final DefaultResourceKeyString that = (DefaultResourceKeyString)o;
        return Objects.equals(qualifier, that.qualifier) && id.equals(that.id);
    }

    @Override public int hashCode() {
        return Objects.hash(qualifier, id);
    }

    @Override public String toString() {
        return ResourceKeyString.toString(qualifier, id);
    }
}
