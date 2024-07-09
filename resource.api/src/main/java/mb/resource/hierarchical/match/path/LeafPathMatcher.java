package mb.resource.hierarchical.match.path;

import mb.resource.hierarchical.ResourcePath;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LeafPathMatcher implements PathMatcher {
    private final String leaf;

    public LeafPathMatcher(String leaf) {
        this.leaf = leaf;
    }

    @Override public boolean matches(ResourcePath path, ResourcePath rootDir) {
        return leaf.equals(path.getLeaf());
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final LeafPathMatcher that = (LeafPathMatcher)o;
        return leaf.equals(that.leaf);
    }

    @Override public int hashCode() {
        return leaf.hashCode();
    }

    @Override public String toString() {
        return "with-leaf(" + leaf + ")";
    }
}
