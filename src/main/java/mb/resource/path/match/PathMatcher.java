package mb.resource.path.match;

import mb.resource.path.Path;

import java.io.Serializable;

@FunctionalInterface
public interface PathMatcher extends Serializable {
    boolean matches(Path path, Path rootDirectoryPath);
}
