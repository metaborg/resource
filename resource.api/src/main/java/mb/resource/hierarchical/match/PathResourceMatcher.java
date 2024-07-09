package mb.resource.hierarchical.match;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.match.path.PathMatcher;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PathResourceMatcher implements ResourceMatcher {
    private final PathMatcher matcher;

    public PathResourceMatcher(PathMatcher matcher) {
        this.matcher = matcher;
    }

    @Override public boolean matches(HierarchicalResource resource, HierarchicalResource rootDirectory) {
        return matcher.matches(resource.getKey(), rootDirectory.getKey());
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final PathResourceMatcher that = (PathResourceMatcher) o;
        return matcher.equals(that.matcher);
    }

    @Override public int hashCode() {
        return matcher.hashCode();
    }

    @Override public String toString() {
        return "with-path-matcher(" + matcher + ")";
    }
}
