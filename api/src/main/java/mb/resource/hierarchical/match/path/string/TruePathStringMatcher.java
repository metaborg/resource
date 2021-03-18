package mb.resource.hierarchical.match.path.string;

import org.checkerframework.checker.nullness.qual.Nullable;

public class TruePathStringMatcher implements PathStringMatcher {
    @Override public boolean matches(String pathString) {
        return true;
    }

    @Override public boolean equals(@Nullable Object o) {
        return this == o || (o != null && getClass() == o.getClass());
    }

    @Override public int hashCode() {
        return 0;
    }

    @Override public String toString() {
        return "TruePathStringMatcher()";
    }
}
