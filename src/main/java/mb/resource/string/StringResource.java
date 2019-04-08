package mb.resource.string;

import mb.resource.Resource;
import mb.resource.ResourceKey;

public class StringResource implements Resource {
    public final String str;
    public final ResourceKey key;

    public StringResource(String str, ResourceKey key) {
        this.str = str;
        this.key = key;
    }

    @Override public ResourceKey getKey() {
        return key;
    }

    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final StringResource that = (StringResource) o;
        return key.equals(that.key);
    }

    @Override public int hashCode() {
        return key.hashCode();
    }

    @Override public String toString() {
        return key.toString();
    }
}
