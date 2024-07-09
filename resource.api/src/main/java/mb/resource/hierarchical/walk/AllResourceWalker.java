package mb.resource.hierarchical.walk;

import mb.resource.hierarchical.HierarchicalResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AllResourceWalker implements ResourceWalker {
    private final List<ResourceWalker> walkers;

    public AllResourceWalker(ArrayList<ResourceWalker> walkers) {
        this.walkers = walkers;
    }

    public AllResourceWalker(ResourceWalker... walkers) {
        this.walkers = Arrays.asList(walkers);
    }

    @Override
    public boolean traverse(HierarchicalResource directory, HierarchicalResource rootDirectory) throws IOException {
        for(ResourceWalker walker : walkers) {
            if(!walker.traverse(directory, rootDirectory)) return false;
        }
        return true;
    }

    @Override public boolean equals(@Nullable Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        final AllResourceWalker other = (AllResourceWalker)obj;
        return walkers.equals(other.walkers);
    }

    @Override public int hashCode() {
        return Objects.hash(walkers);
    }

    @Override public String toString() {
        return "all(" + walkers + ")";
    }
}
