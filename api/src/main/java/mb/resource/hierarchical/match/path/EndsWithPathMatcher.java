package mb.resource.hierarchical.match.path;

import mb.resource.hierarchical.ResourcePath;
import mb.resource.util.SeparatorUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EndsWithPathMatcher implements PathMatcher {
    private final String suffix;

    public EndsWithPathMatcher(String suffix) {
        this.suffix = suffix;
    }

    @Override public boolean matches(ResourcePath path, ResourcePath rootDir) {
        final String relative = SeparatorUtil.convertCurrentToUnixSeparator(rootDir.relativize(path));
        return relative.endsWith(suffix);
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final EndsWithPathMatcher that = (EndsWithPathMatcher)o;
        return suffix.equals(that.suffix);
    }

    @Override public int hashCode() {
        return suffix.hashCode();
    }

    @Override public String toString() {
        return "ends-with(" + suffix + ")";
    }
}
