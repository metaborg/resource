package mb.resource.hierarchical.walk;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.match.path.PathMatcher;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface ResourceWalker extends Serializable {
    boolean traverse(HierarchicalResource directory, HierarchicalResource rootDirectory) throws IOException;


    static TrueResourceWalker ofTrue() {
        return new TrueResourceWalker();
    }

    static FalseResourceWalker ofFalse() {
        return new FalseResourceWalker();
    }

    static PathResourceWalker ofPath(PathMatcher matcher) {
        return new PathResourceWalker(matcher);
    }


    static PathResourceWalker ofNoHidden() { return ofPath(PathMatcher.ofNoHidden()); }
}
