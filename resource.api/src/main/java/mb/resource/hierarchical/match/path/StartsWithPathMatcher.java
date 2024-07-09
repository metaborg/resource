package mb.resource.hierarchical.match.path;

import mb.resource.hierarchical.ResourcePath;
import mb.resource.util.SeparatorUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

public class StartsWithPathMatcher implements PathMatcher {
    private final String prefix;

    public StartsWithPathMatcher(String prefix) {
        this.prefix = prefix;
    }

    @Override public boolean matches(ResourcePath path, ResourcePath rootDir) {
        final String relative = SeparatorUtil.convertCurrentToUnixSeparator(rootDir.relativize(path));
        return relative.startsWith(prefix);
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final StartsWithPathMatcher that = (StartsWithPathMatcher)o;
        return prefix.equals(that.prefix);
    }

    @Override public int hashCode() {
        return prefix.hashCode();
    }

    @Override public String toString() {
        return "starts-with(" + prefix + ")";
    }
}
