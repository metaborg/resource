package mb.resource.walk;

import mb.resource.TreeResource;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AllResourceWalker implements ResourceWalker {
    @Override public boolean traverse(TreeResource directory, TreeResource rootDirectory) {
        return true;
    }

    @Override public boolean equals(@Nullable Object o) {
        return this == o || (o != null && getClass() == o.getClass());
    }

    @Override public int hashCode() {
        return 0;
    }

    @Override public String toString() {
        return "AllResourceWalker()";
    }
}
