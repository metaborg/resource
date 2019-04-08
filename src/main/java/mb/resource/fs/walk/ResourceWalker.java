package mb.resource.fs.walk;

import mb.resource.fs.FSResource;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface ResourceWalker extends Serializable {
    boolean traverse(FSResource directory, FSResource rootDirectory) throws IOException;
}
