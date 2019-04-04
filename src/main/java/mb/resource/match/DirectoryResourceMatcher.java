package mb.resource.match;

import mb.resource.TreeResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;

public class DirectoryResourceMatcher implements ResourceMatcher {
    @Override public boolean matches(TreeResource resource, TreeResource rootDirectory) throws IOException {
        return resource.isDirectory();
    }

    @Override public boolean equals(@Nullable Object o) {
        return this == o || (o != null && getClass() == o.getClass());
    }

    @Override public int hashCode() {
        return 0;
    }

    @Override public String toString() {
        return "DirectoryResourceMatcher()";
    }
}
