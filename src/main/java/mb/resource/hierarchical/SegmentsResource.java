package mb.resource.hierarchical;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

public abstract class SegmentsResource<SELF extends SegmentsResource<SELF>> extends HierarchicalResourceDefaults<SELF> implements HierarchicalResource {
    protected final SegmentsPath path;

    protected SegmentsResource(SegmentsPath path) {
        this.path = path;
    }


    @Override public SegmentsPath getKey() {
        return path;
    }

    @Override public SegmentsPath getPath() {
        return path;
    }


    @Override public @Nullable SELF getParent() {
        final @Nullable SegmentsPath parent = path.getParent();
        if(parent == null) return null;
        return create(parent);
    }

    @Override public @Nullable SELF getRoot() {
        final @Nullable SegmentsPath root = path.getRoot();
        if(root == null) return null;
        return create(root);
    }


    @Override public SELF appendSegment(String segment) {
        return create(path.appendSegment(segment));
    }

    @Override public SELF appendSegments(Iterable<String> segments) {
        return create(path.appendSegments(segments));
    }

    @Override public SELF appendSegments(Collection<String> segments) {
        return create(path.appendSegments(segments));
    }


    @Override public SELF appendRelativePath(String relativePath) {
        return create(path.appendRelativePath(relativePath));
    }

    @Override public SELF appendOrReplaceWithPath(String other) {
        return create(path.appendOrReplaceWithPath(other));
    }

    @Override public SELF appendString(String other) {
        return create(path.appendString(other));
    }

    @Override public SELF appendRelativePath(ResourcePath relativePath) {
        return create(path.appendRelativePath(relativePath));
    }


    @Override public SELF replaceLeaf(String segment) {
        return create(path.replaceLeaf(segment));
    }


    /**
     * Returns {@code this}. Required because {@code this} is not of type {@code SELF}.
     */
    protected abstract SELF self();

    /**
     * Creates a new resource with given {@code path} of type {@code SELF}.
     */
    protected abstract SELF create(SegmentsPath path);


    @Override public int hashCode() { return path.hashCode(); }

    @Override public String toString() {
        return path.toString();
    }
}
