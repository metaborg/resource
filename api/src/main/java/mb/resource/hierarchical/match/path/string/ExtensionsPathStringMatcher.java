package mb.resource.hierarchical.match.path.string;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class ExtensionsPathStringMatcher implements PathStringMatcher {
    private final List<String> extensions;
    private transient LinkedHashSet<String> extensionsHashSet;

    public ExtensionsPathStringMatcher(LinkedHashSet<String> extensions) {
        this.extensions = new ArrayList<>(extensions);
        this.extensionsHashSet = extensions;
    }

    public ExtensionsPathStringMatcher(ArrayList<String> extensions) {
        this.extensions = extensions;
        this.extensionsHashSet = new LinkedHashSet<>(extensions);
    }

    public ExtensionsPathStringMatcher(Collection<String> extensions) {
        this.extensions = new ArrayList<>(extensions);
        this.extensionsHashSet = new LinkedHashSet<>(extensions);
    }

    public ExtensionsPathStringMatcher(String... extensions) {
        this.extensions = Arrays.asList(extensions);
        this.extensionsHashSet = new LinkedHashSet<>(this.extensions);
    }

    @Override public boolean matches(String pathString) {
        final int dotIndex = pathString.lastIndexOf('.');
        if(dotIndex == -1) {
            return false;
        }
        final String extension = pathString.substring(dotIndex + 1);
        return extensionsHashSet.contains(extension);
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ExtensionsPathStringMatcher that = (ExtensionsPathStringMatcher)o;
        return extensions.equals(that.extensions);
    }

    @Override public int hashCode() {
        return extensions.hashCode();
    }

    @Override public String toString() {
        return "ExtensionsPathStringMatcher(" + extensions + ")";
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        extensionsHashSet = new LinkedHashSet<>(extensions);
    }
}
