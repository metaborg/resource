package mb.resource.fs.path.match;

import mb.resource.fs.FSPath;
import org.checkerframework.checker.nullness.qual.Nullable;

public class NoHiddenPathMatcher implements PathMatcher {
    @Override public boolean matches(FSPath path, FSPath rootDir) {
        final @Nullable String leaf = path.getLeaf();
        if(leaf == null) {
            return false;
        }
        return !leaf.startsWith(".");
    }

    @Override public boolean equals(@Nullable Object o) {
        return this == o || (o != null && getClass() == o.getClass());
    }

    @Override public int hashCode() {
        return 0;
    }

    @Override public String toString() {
        return "NoHiddenPathMatcher()";
    }
}
