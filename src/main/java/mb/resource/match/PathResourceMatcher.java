package mb.resource.match;

import mb.resource.TreeResource;
import mb.resource.path.match.PathMatcher;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PathResourceMatcher implements ResourceMatcher {
    private final PathMatcher matcher;

    public PathResourceMatcher(PathMatcher matcher) {
        this.matcher = matcher;
    }

    @Override public boolean matches(TreeResource resource, TreeResource rootDirectory) {
        return matcher.matches(resource.getPath(), rootDirectory.getPath());
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
        return "PathResourceMatcher(" + matcher + ")";
    }
}
