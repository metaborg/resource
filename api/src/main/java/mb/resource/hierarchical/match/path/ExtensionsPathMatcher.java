package mb.resource.hierarchical.match.path;

import mb.resource.hierarchical.ResourcePath;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class ExtensionsPathMatcher implements PathMatcher {
    private final List<String> extensions;
    private transient HashSet<String> extensionsHashSet;

    public ExtensionsPathMatcher(HashSet<String> extensions) {
        this.extensions = new ArrayList<>(extensions);
        this.extensionsHashSet = extensions;
    }

    public ExtensionsPathMatcher(ArrayList<String> extensions) {
        this.extensions = extensions;
        this.extensionsHashSet = new HashSet<>(extensions);
    }

    public ExtensionsPathMatcher(Collection<String> extensions) {
        this.extensions = new ArrayList<>(extensions);
        this.extensionsHashSet = new HashSet<>(extensions);
    }

    public ExtensionsPathMatcher(String... extensions) {
        this.extensions = Arrays.asList(extensions);
        this.extensionsHashSet = new HashSet<>(this.extensions);
    }

    @Override public boolean matches(ResourcePath path, ResourcePath rootDir) {
        final @Nullable String extension = path.getLeafExtension();
        if(extension == null) {
            return false;
        }
        return extensionsHashSet.contains(extension);
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ExtensionsPathMatcher that = (ExtensionsPathMatcher) o;
        return extensions.equals(that.extensions);
    }

    @Override public int hashCode() {
        return extensions.hashCode();
    }

    @Override public String toString() {
        return "ExtensionsPathMatcher(" + extensions + ")";
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        extensionsHashSet = new HashSet<>(extensions);
    }
}
