package mb.resource.hierarchical.walk;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.match.path.PathMatcher;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PathResourceWalker implements ResourceWalker {
    private final PathMatcher pathMatcher;

    public PathResourceWalker(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    @Override public boolean traverse(HierarchicalResource resource, HierarchicalResource rootDirectory) {
        return pathMatcher.matches(resource.getPath(), rootDirectory.getPath());
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final PathResourceWalker that = (PathResourceWalker) o;
        return pathMatcher.equals(that.pathMatcher);
    }

    @Override public int hashCode() {
        return pathMatcher.hashCode();
    }

    @Override public String toString() {
        return "with-path-matcher(" + pathMatcher + ")";
    }
}
