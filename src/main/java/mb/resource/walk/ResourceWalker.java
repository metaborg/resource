package mb.resource.walk;

import mb.resource.TreeResource;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface ResourceWalker extends Serializable {
    boolean traverse(TreeResource directory, TreeResource rootDirectory) throws IOException;
}
