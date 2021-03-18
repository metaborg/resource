package mb.resource.hierarchical.match.path;

import mb.resource.hierarchical.ResourcePath;
import mb.resource.util.AntPattern;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AntPatternPathMatcher implements PathMatcher {
    private final AntPattern pattern;

    public AntPatternPathMatcher(AntPattern pattern) {
        this.pattern = pattern;
    }

    public AntPatternPathMatcher(String pattern) {
        this(new AntPattern(pattern));
    }

    @Override public boolean matches(ResourcePath path, ResourcePath rootDir) {
        final String relative = rootDir.relativize(path);
        return pattern.match(relative);
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final AntPatternPathMatcher that = (AntPatternPathMatcher)o;
        return pattern.equals(that.pattern);
    }

    @Override public int hashCode() {
        return pattern.hashCode();
    }

    @Override public String toString() {
        return "PatternPathMatcher(" + pattern + ")";
    }
}
