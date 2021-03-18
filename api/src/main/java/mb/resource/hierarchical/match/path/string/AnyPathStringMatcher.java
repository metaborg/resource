package mb.resource.hierarchical.match.path.string;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AnyPathStringMatcher implements PathStringMatcher {
    private final List<PathStringMatcher> matchers;

    public AnyPathStringMatcher(ArrayList<PathStringMatcher> matchers) {
        this.matchers = matchers;
    }

    public AnyPathStringMatcher(PathStringMatcher... matchers) {
        this.matchers = Arrays.asList(matchers);
    }

    @Override
    public boolean matches(String pathString) {
        for(PathStringMatcher matcher : matchers) {
            if(matcher.matches(pathString)) return true;
        }
        return false;
    }

    @Override public boolean equals(@Nullable Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        final AnyPathStringMatcher other = (AnyPathStringMatcher)obj;
        return matchers.equals(other.matchers);
    }

    @Override public int hashCode() {
        return Objects.hash(matchers);
    }

    @Override public String toString() {
        return "AnyPathStringMatcher(" + matchers.toString() + ")";
    }
}
