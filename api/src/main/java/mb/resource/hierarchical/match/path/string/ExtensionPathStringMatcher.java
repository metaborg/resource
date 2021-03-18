package mb.resource.hierarchical.match.path.string;

import org.checkerframework.checker.nullness.qual.Nullable;

public class ExtensionPathStringMatcher implements PathStringMatcher {
    private final String extension;

    public ExtensionPathStringMatcher(String extension) {
        this.extension = extension;
    }

    @Override public boolean matches(String pathString) {
        final int dotIndex = pathString.lastIndexOf('.');
        if(dotIndex == -1) {
            return false;
        }
        final String extension = pathString.substring(dotIndex + 1);
        return this.extension.equals(extension);
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ExtensionPathStringMatcher that = (ExtensionPathStringMatcher)o;
        return extension.equals(that.extension);
    }

    @Override public int hashCode() {
        return extension.hashCode();
    }

    @Override public String toString() {
        return "ExtensionPathStringMatcher(" + extension + ")";
    }
}
