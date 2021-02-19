package mb.resource.hierarchical.walk;

import mb.resource.hierarchical.HierarchicalResource;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface ResourceWalker extends Serializable {
    boolean traverse(HierarchicalResource directory, HierarchicalResource rootDirectory) throws IOException;
}
