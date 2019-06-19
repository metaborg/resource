package mb.resource.hierarchical.match.path;

import mb.resource.hierarchical.ResourcePath;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;

public class PatternsPathMatcher implements PathMatcher {
    private final ArrayList<AntPattern> patterns;

    public PatternsPathMatcher(Iterable<String> patterns) {
        this.patterns = new ArrayList<>();
        for(String pattern : patterns) {
            this.patterns.add(new AntPattern(pattern));
        }
    }

    @Override public boolean matches(ResourcePath path, ResourcePath rootDir) {
        final String relative = rootDir.relativize(path).toString();
        for(AntPattern pattern : patterns) {
            if(pattern.match(relative)) {
                return true;
            }
        }
        return false;
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final PatternsPathMatcher that = (PatternsPathMatcher) o;
        return patterns.equals(that.patterns);
    }

    @Override public int hashCode() {
        return patterns.hashCode();
    }

    @Override public String toString() {
        return "PatternsPathMatcher(" + patterns + ")";
    }
}
