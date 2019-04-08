package mb.resource.fs.walk;

import mb.resource.fs.FSResource;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AllResourceWalker implements ResourceWalker {
    @Override public boolean traverse(FSResource directory, FSResource rootDirectory) {
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
