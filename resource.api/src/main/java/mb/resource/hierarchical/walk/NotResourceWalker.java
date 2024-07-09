package mb.resource.hierarchical.walk;

import mb.resource.hierarchical.HierarchicalResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.Objects;

public class NotResourceWalker implements ResourceWalker {
    private final ResourceWalker walker;

    public NotResourceWalker(ResourceWalker walker) {
        this.walker = walker;
    }

    @Override
    public boolean traverse(HierarchicalResource directory, HierarchicalResource rootDirectory) throws IOException {
        return !walker.traverse(directory, rootDirectory);
    }

    @Override public boolean equals(@Nullable Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        final NotResourceWalker other = (NotResourceWalker)obj;
        return walker.equals(other.walker);
    }

    @Override public int hashCode() {
        return Objects.hash(walker);
    }

    @Override public String toString() {
        return "!" + walker;
    }
}
