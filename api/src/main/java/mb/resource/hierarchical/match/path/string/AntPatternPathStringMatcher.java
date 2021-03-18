package mb.resource.hierarchical.match.path.string;

import mb.resource.util.AntPattern;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AntPatternPathStringMatcher implements PathStringMatcher {
    private final AntPattern pattern;

    public AntPatternPathStringMatcher(AntPattern pattern) {
        this.pattern = pattern;
    }

    public AntPatternPathStringMatcher(String pattern) {
        this(new AntPattern(pattern));
    }

    @Override public boolean matches(String pathString) {
        return pattern.match(pathString);
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final AntPatternPathStringMatcher that = (AntPatternPathStringMatcher)o;
        return pattern.equals(that.pattern);
    }

    @Override public int hashCode() {
        return pattern.hashCode();
    }

    @Override public String toString() {
        return "AntPatternPathStringMatcher(" + pattern + ')';
    }
}
