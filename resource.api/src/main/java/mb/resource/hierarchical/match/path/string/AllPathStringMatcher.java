package mb.resource.hierarchical.match.path.string;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AllPathStringMatcher implements PathStringMatcher {
    private final List<PathStringMatcher> matchers;

    public AllPathStringMatcher(ArrayList<PathStringMatcher> matchers) {
        this.matchers = matchers;
    }

    public AllPathStringMatcher(PathStringMatcher... matchers) {
        this.matchers = Arrays.asList(matchers);
    }

    @Override
    public boolean matches(String pathString) {
        for(PathStringMatcher matcher : matchers) {
            if(!matcher.matches(pathString)) return false;
        }
        return true;
    }

    @Override public boolean equals(@Nullable Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        final AllPathStringMatcher other = (AllPathStringMatcher)obj;
        return matchers.equals(other.matchers);
    }

    @Override public int hashCode() {
        return Objects.hash(matchers);
    }

    @Override public String toString() {
        return "AllPathStringMatcher(" + matchers.toString() + ")";
    }
}
