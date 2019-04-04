package mb.resource.path.match;

import mb.resource.path.Path;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ExtensionPathMatcher implements PathMatcher {
    private final String extension;

    public ExtensionPathMatcher(String extension) {
        this.extension = extension;
    }

    @Override public boolean matches(Path path, Path rootDir) {
        final @Nullable String extension = path.getLeafExtension();
        if(extension == null) {
            return false;
        }
        return this.extension.equals(extension);
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ExtensionPathMatcher that = (ExtensionPathMatcher) o;
        return extension.equals(that.extension);
    }

    @Override public int hashCode() {
        return extension.hashCode();
    }

    @Override public String toString() {
        return "ExtensionPathMatcher(" + extension + ")";
    }
}
