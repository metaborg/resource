package mb.resource.hierarchical.match.path;

import mb.resource.hierarchical.ResourcePath;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class ExtensionsPathMatcher implements PathMatcher {
    private final List<String> extensions;
    private transient LinkedHashSet<String> extensionsHashSet;

    public ExtensionsPathMatcher(LinkedHashSet<String> extensions) {
        this.extensions = new ArrayList<>(extensions);
        this.extensionsHashSet = extensions;
    }

    public ExtensionsPathMatcher(ArrayList<String> extensions) {
        this.extensions = extensions;
        this.extensionsHashSet = new LinkedHashSet<>(extensions);
    }

    public ExtensionsPathMatcher(Collection<String> extensions) {
        this.extensions = new ArrayList<>(extensions);
        this.extensionsHashSet = new LinkedHashSet<>(extensions);
    }

    public ExtensionsPathMatcher(String... extensions) {
        this.extensions = Arrays.asList(extensions);
        this.extensionsHashSet = new LinkedHashSet<>(this.extensions);
    }

    @Override public boolean matches(ResourcePath path, ResourcePath rootDir) {
        final @Nullable String extension = path.getLeafFileExtension();
        if(extension == null) {
            return false;
        }
        return extensionsHashSet.contains(extension);
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ExtensionsPathMatcher that = (ExtensionsPathMatcher)o;
        return extensions.equals(that.extensions);
    }

    @Override public int hashCode() {
        return extensions.hashCode();
    }

    @Override public String toString() {
        return "with-any-extension(" + extensions + ")";
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        extensionsHashSet = new LinkedHashSet<>(extensions);
    }
}
