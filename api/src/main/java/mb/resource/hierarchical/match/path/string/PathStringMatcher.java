package mb.resource.hierarchical.match.path.string;

import mb.resource.util.AntPattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

@FunctionalInterface
public interface PathStringMatcher extends Serializable {
    boolean matches(String pathString);


    static TruePathStringMatcher ofTrue() {
        return new TruePathStringMatcher();
    }

    static FalsePathStringMatcher ofFalse() {
        return new FalsePathStringMatcher();
    }

    static AllPathStringMatcher ofAll(PathStringMatcher... matchers) {
        return new AllPathStringMatcher(matchers);
    }

    static AnyPathStringMatcher ofAny(PathStringMatcher... matchers) {
        return new AnyPathStringMatcher(matchers);
    }

    static NotPathStringMatcher ofNot(PathStringMatcher matcher) {
        return new NotPathStringMatcher(matcher);
    }

    static ExtensionPathStringMatcher ofExtension(String extension) {
        return new ExtensionPathStringMatcher(extension);
    }

    static ExtensionsPathStringMatcher ofExtensions(String... extensions) {
        return new ExtensionsPathStringMatcher(extensions);
    }

    static AntPatternPathStringMatcher ofAntPattern(AntPattern antPattern) {
        return new AntPatternPathStringMatcher(antPattern);
    }

    static AntPatternPathStringMatcher ofAntPattern(String antPattern) {
        return new AntPatternPathStringMatcher(antPattern);
    }

    static AntPatternsPathStringMatcher ofAntPatterns(AntPattern... antPatterns) {
        return new AntPatternsPathStringMatcher(antPatterns);
    }

    static AntPatternsPathStringMatcher ofAntPatterns(String... antPatterns) {
        return new AntPatternsPathStringMatcher(antPatterns);
    }

    static RegexPathStringMatcher ofRegex(String regex) {
        return new RegexPathStringMatcher(regex);
    }


    default NotPathStringMatcher not() {
        return new NotPathStringMatcher(this);
    }

    default AllPathStringMatcher and(PathStringMatcher... matchers) {
        final ArrayList<PathStringMatcher> allMatchers = new ArrayList<>(matchers.length + 1);
        allMatchers.add(this);
        Collections.addAll(allMatchers, matchers);
        return new AllPathStringMatcher(allMatchers);
    }

    default AnyPathStringMatcher or(PathStringMatcher... matchers) {
        final ArrayList<PathStringMatcher> anyMatchers = new ArrayList<>(matchers.length + 1);
        anyMatchers.add(this);
        Collections.addAll(anyMatchers, matchers);
        return new AnyPathStringMatcher(anyMatchers);
    }
}
