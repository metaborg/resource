package mb.resource.hierarchical.match.path;

import mb.resource.hierarchical.ResourcePath;

import java.io.Serializable;

@FunctionalInterface
public interface PathMatcher extends Serializable {
    boolean matches(ResourcePath path, ResourcePath rootDirectoryPath);
}
