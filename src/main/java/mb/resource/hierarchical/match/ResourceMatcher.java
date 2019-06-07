package mb.resource.hierarchical.match;

import mb.resource.hierarchical.HierarchicalResource;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface ResourceMatcher extends Serializable {
    boolean matches(HierarchicalResource resource, HierarchicalResource rootDirectory) throws IOException;
}
