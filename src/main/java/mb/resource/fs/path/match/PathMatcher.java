package mb.resource.fs.path.match;

import mb.resource.fs.FSPath;

import java.io.Serializable;

@FunctionalInterface
public interface PathMatcher extends Serializable {
    boolean matches(FSPath path, FSPath rootDirectoryPath);
}
