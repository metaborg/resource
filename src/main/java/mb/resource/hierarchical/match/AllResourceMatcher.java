package mb.resource.hierarchical.match;

import mb.resource.hierarchical.HierarchicalResource;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AllResourceMatcher implements ResourceMatcher {
    @Override public boolean matches(HierarchicalResource resource, HierarchicalResource rootDirectory) {
        return true;
    }

    @Override public boolean equals(@Nullable Object o) {
        return this == o || (o != null && getClass() == o.getClass());
    }

    @Override public int hashCode() {
        return 0;
    }

    @Override public String toString() {
        return "AllResourceMatcher()";
    }
}
