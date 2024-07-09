package mb.resource.hierarchical.match.path;

import mb.resource.hierarchical.ResourcePath;
import mb.resource.util.AntPattern;
import mb.resource.util.SeparatorUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class AntPatternsPathMatcher implements PathMatcher {
    private final ArrayList<AntPattern> patterns;


    public AntPatternsPathMatcher(ArrayList<AntPattern> patterns) {
        this.patterns = patterns;
    }

    public AntPatternsPathMatcher(Iterable<AntPattern> patterns) {
        this.patterns = new ArrayList<>();
        for(AntPattern pattern : patterns) {
            this.patterns.add(pattern);
        }
    }

    public AntPatternsPathMatcher(AntPattern... patterns) {
        this.patterns = new ArrayList<>();
        Collections.addAll(this.patterns, patterns);
    }

    public AntPatternsPathMatcher(String... patterns) {
        this.patterns = new ArrayList<>();
        for(String pattern : patterns) {
            this.patterns.add(new AntPattern(pattern));
        }
    }


    @Override public boolean matches(ResourcePath path, ResourcePath rootDir) {
        final String relative = SeparatorUtil.convertCurrentToUnixSeparator(rootDir.relativize(path));
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
        final AntPatternsPathMatcher that = (AntPatternsPathMatcher)o;
        return patterns.equals(that.patterns);
    }

    @Override public int hashCode() {
        return patterns.hashCode();
    }

    @Override public String toString() {
        return "with-any-ant-patterns(" + patterns + ")";
    }
}
