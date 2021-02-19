package mb.resource.hierarchical.walk;

import mb.resource.hierarchical.HierarchicalResource;
import org.checkerframework.checker.nullness.qual.Nullable;

public class FalseResourceWalker implements ResourceWalker {
    @Override public boolean traverse(HierarchicalResource directory, HierarchicalResource rootDirectory) {
        return false;
    }

    @Override public boolean equals(@Nullable Object o) {
        return this == o || (o != null && getClass() == o.getClass());
    }

    @Override public int hashCode() {
        return 0;
    }

    @Override public String toString() {
        return "FalseResourceWalker()";
    }
}
