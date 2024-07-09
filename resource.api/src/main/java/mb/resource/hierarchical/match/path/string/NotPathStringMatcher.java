package mb.resource.hierarchical.match.path.string;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public class NotPathStringMatcher implements PathStringMatcher {
    private final PathStringMatcher matcher;

    public NotPathStringMatcher(PathStringMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(String pathString) {
        return !matcher.matches(pathString);
    }

    @Override public boolean equals(@Nullable Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        final NotPathStringMatcher other = (NotPathStringMatcher)obj;
        return matcher.equals(other.matcher);
    }

    @Override public int hashCode() {
        return Objects.hash(matcher);
    }

    @Override public String toString() {
        return "NotPathStringMatcher(" + matcher.toString() + ")";
    }
}
