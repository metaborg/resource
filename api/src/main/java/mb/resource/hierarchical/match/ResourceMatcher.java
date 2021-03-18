package mb.resource.hierarchical.match;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.match.path.PathMatcher;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

@FunctionalInterface
public interface ResourceMatcher extends Serializable {
    boolean matches(HierarchicalResource resource, HierarchicalResource rootDirectory) throws IOException;


    static TrueResourceMatcher ofTrue() {
        return new TrueResourceMatcher();
    }

    static FalseResourceMatcher ofFalse() {
        return new FalseResourceMatcher();
    }

    static AllResourceMatcher ofAll(ResourceMatcher... matchers) {
        return new AllResourceMatcher(matchers);
    }

    static AnyResourceMatcher ofAny(ResourceMatcher... matchers) {
        return new AnyResourceMatcher(matchers);
    }

    static NotResourceMatcher ofNot(ResourceMatcher matcher) {
        return new NotResourceMatcher(matcher);
    }

    static PathResourceMatcher ofPath(PathMatcher matcher) {
        return new PathResourceMatcher(matcher);
    }

    static DirectoryResourceMatcher ofDirectory() {
        return new DirectoryResourceMatcher();
    }

    static FileResourceMatcher ofFile() {
        return new FileResourceMatcher();
    }


    default NotResourceMatcher not() {
        return new NotResourceMatcher(this);
    }

    default AllResourceMatcher and(ResourceMatcher... matchers) {
        final ArrayList<ResourceMatcher> allMatchers = new ArrayList<>(matchers.length + 1);
        allMatchers.add(this);
        Collections.addAll(allMatchers, matchers);
        return new AllResourceMatcher(allMatchers);
    }

    default AnyResourceMatcher or(ResourceMatcher... matchers) {
        final ArrayList<ResourceMatcher> anyMatchers = new ArrayList<>(matchers.length + 1);
        anyMatchers.add(this);
        Collections.addAll(anyMatchers, matchers);
        return new AnyResourceMatcher(anyMatchers);
    }
}
