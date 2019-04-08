package mb.resource.fs.match;

import mb.resource.fs.FSResource;
import mb.resource.fs.path.match.PathMatcher;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PathResourceMatcher implements ResourceMatcher {
    private final PathMatcher matcher;

    public PathResourceMatcher(PathMatcher matcher) {
        this.matcher = matcher;
    }

    @Override public boolean matches(FSResource resource, FSResource rootDirectory) {
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
