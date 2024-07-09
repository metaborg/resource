package mb.resource.hierarchical.walk;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.match.path.PathMatcher;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

@FunctionalInterface
public interface ResourceWalker extends Serializable {
    boolean traverse(HierarchicalResource directory, HierarchicalResource rootDirectory) throws IOException;


    static TrueResourceWalker ofTrue() {
        return new TrueResourceWalker();
    }

    static FalseResourceWalker ofFalse() {
        return new FalseResourceWalker();
    }

    static AllResourceWalker ofAll(ResourceWalker... walkers) {
        return new AllResourceWalker(walkers);
    }

    static AnyResourceWalker ofAny(ResourceWalker... walkers) {
        return new AnyResourceWalker(walkers);
    }

    static NotResourceWalker ofNot(ResourceWalker walkers) {
        return new NotResourceWalker(walkers);
    }

    static PathResourceWalker ofPath(PathMatcher matcher) {
        return new PathResourceWalker(matcher);
    }


    default NotResourceWalker not() {
        return new NotResourceWalker(this);
    }

    default AllResourceWalker and(ResourceWalker... walkers) {
        final ArrayList<ResourceWalker> allWalkers = new ArrayList<>(walkers.length + 1);
        allWalkers.add(this);
        Collections.addAll(allWalkers, walkers);
        return new AllResourceWalker(allWalkers);
    }

    default AnyResourceWalker or(ResourceWalker... walkers) {
        final ArrayList<ResourceWalker> anyWalkers = new ArrayList<>(walkers.length + 1);
        anyWalkers.add(this);
        Collections.addAll(anyWalkers, walkers);
        return new AnyResourceWalker(anyWalkers);
    }


    static PathResourceWalker ofNoHidden() {return ofPath(PathMatcher.ofNoHidden());}
}
