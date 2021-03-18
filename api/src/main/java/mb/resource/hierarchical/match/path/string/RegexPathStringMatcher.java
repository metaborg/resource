package mb.resource.hierarchical.match.path.string;

import mb.resource.hierarchical.ResourcePath;
import mb.resource.hierarchical.match.path.PathMatcher;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.regex.Pattern;

public class RegexPathStringMatcher implements PathStringMatcher {
    private final String pattern;
    private transient Pattern compiledPattern;

    public RegexPathStringMatcher(String pattern) {
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    @Override public boolean matches(String pathString) {
        return compiledPattern.matcher(pathString).matches();
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final RegexPathStringMatcher that = (RegexPathStringMatcher) o;
        return pattern.equals(that.pattern);
    }

    @Override public int hashCode() {
        return pattern.hashCode();
    }

    @Override public String toString() {
        return "RegexPathStringMatcher(" + pattern + ")";
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        compiledPattern = Pattern.compile(pattern);
    }
}
