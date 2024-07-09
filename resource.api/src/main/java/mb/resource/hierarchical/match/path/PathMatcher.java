package mb.resource.hierarchical.match.path;

import mb.resource.hierarchical.ResourcePath;
import mb.resource.util.AntPattern;

import java.io.Serializable;

@FunctionalInterface
public interface PathMatcher extends Serializable {
    boolean matches(ResourcePath path, ResourcePath rootDirectoryPath);


    static ExtensionPathMatcher ofExtension(String extension) {
        return new ExtensionPathMatcher(extension);
    }

    static ExtensionsPathMatcher ofExtensions(String... extensions) {
        return new ExtensionsPathMatcher(extensions);
    }

    static StartsWithPathMatcher ofStartsWith(String prefix) {
        return new StartsWithPathMatcher(prefix);
    }

    static EndsWithPathMatcher ofEndsWith(String suffix) {
        return new EndsWithPathMatcher(suffix);
    }

    static LeafPathMatcher ofLeaf(String leaf) {
        return new LeafPathMatcher(leaf);
    }

    static AntPatternPathMatcher ofAntPattern(AntPattern antPattern) {
        return new AntPatternPathMatcher(antPattern);
    }

    static AntPatternPathMatcher ofAntPattern(String antPattern) {
        return new AntPatternPathMatcher(antPattern);
    }

    static AntPatternsPathMatcher ofAntPatterns(AntPattern... antPatterns) {
        return new AntPatternsPathMatcher(antPatterns);
    }

    static AntPatternsPathMatcher ofAntPatterns(String... antPatterns) {
        return new AntPatternsPathMatcher(antPatterns);
    }

    static RegexPathMatcher ofRegex(String regex) {
        return new RegexPathMatcher(regex);
    }

    static NoHiddenPathMatcher ofNoHidden() {
        return new NoHiddenPathMatcher();
    }
}
