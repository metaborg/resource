package mb.resource.hierarchical.match.path.string;

import mb.resource.util.AntPattern;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class AntPatternsPathStringMatcher implements PathStringMatcher {
    private final ArrayList<AntPattern> patterns;


    public AntPatternsPathStringMatcher(ArrayList<AntPattern> patterns) {
        this.patterns = patterns;
    }

    public AntPatternsPathStringMatcher(Iterable<AntPattern> patterns) {
        this.patterns = new ArrayList<>();
        for(AntPattern pattern : patterns) {
            this.patterns.add(pattern);
        }
    }

    public AntPatternsPathStringMatcher(AntPattern... patterns) {
        this.patterns = new ArrayList<>();
        Collections.addAll(this.patterns, patterns);
    }

    public AntPatternsPathStringMatcher(String... patterns) {
        this.patterns = new ArrayList<>();
        for(String pattern : patterns) {
            this.patterns.add(new AntPattern(pattern));
        }
    }


    @Override public boolean matches(String pathString) {
        for(AntPattern pattern : patterns) {
            if(pattern.match(pathString)) {
                return true;
            }
        }
        return false;
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final AntPatternsPathStringMatcher that = (AntPatternsPathStringMatcher)o;
        return patterns.equals(that.patterns);
    }

    @Override public int hashCode() {
        return patterns.hashCode();
    }

    @Override public String toString() {
        return "AntPatternsPathStringMatcher(" + patterns + ')';
    }
}
