package mb.resource.match;

import mb.resource.TreeResource;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface ResourceMatcher extends Serializable {
    boolean matches(TreeResource resource, TreeResource rootDirectory) throws IOException;
}
