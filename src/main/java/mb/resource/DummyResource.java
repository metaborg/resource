package mb.resource;

import java.util.Objects;

/**
 * Dummy implementation of {@link Resource}, for testing purposes.
 */
public class DummyResource implements Resource {
    private final ResourceKey key;

    public DummyResource(ResourceKey key) {
        this.key = key;
    }

    @Override public ResourceKey getKey() {
        return key;
    }

    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final DummyResource that = (DummyResource)o;
        return key.equals(that.key);
    }

    @Override public int hashCode() {
        return Objects.hash(key);
    }

    @Override public String toString() {
        return "DummyResource{key=" + key + '}';
    }
}
