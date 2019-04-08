package mb.resource.fs.match;

import mb.resource.fs.FSResource;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface ResourceMatcher extends Serializable {
    boolean matches(FSResource resource, FSResource rootDirectory) throws IOException;
}
