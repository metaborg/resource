package mb.resource.string;

import mb.resource.Resource;
import mb.resource.ResourceKey;

import java.nio.charset.Charset;

public class StringResource extends StringIORead implements Resource {
    private final ResourceKey key;

    public StringResource(String text, ResourceKey key) {
        super(text);
        this.key = key;
    }

    public StringResource(String text, Charset charset, ResourceKey key) {
        super(text, charset);
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
