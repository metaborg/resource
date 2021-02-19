package mb.resource.hierarchical;

import mb.resource.ResourceRuntimeException;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Objects;

public class SegmentsPath extends ResourcePathDefaults<SegmentsPath> implements ResourcePath {
    private final String qualifier;
    private final SegmentsIdentifier id;

    public SegmentsPath(String qualifier, SegmentsIdentifier id) {
        this.qualifier = qualifier;
        this.id = id;
    }

    public SegmentsPath(String qualifier, String id) {
        this.qualifier = qualifier;
        this.id = SegmentsIdentifier.fromString(id);
    }


    @Override public String getQualifier() {
        return qualifier;
    }

    @Override public SegmentsIdentifier getId() {
        return id;
    }

    @Override public String getIdAsString() {
        return getId().toString();
    }


    @Override public boolean isAbsolute() {
        return id.isAbsolute();
    }


    @Override public int getSegmentCount() {
        return id.getSegmentCount();
    }

    @Override public Iterable<String> getSegments() {
        return id.getSegments();
    }


    @Override public boolean startsWith(ResourcePath prefix) {
        if(!(prefix instanceof SegmentsPath)) {
            throw new ResourceRuntimeException("Cannot check if this path starts with '" + prefix + "', it is not a SegmentsPath");
        }
        return startsWith((SegmentsPath)prefix);
    }

    public boolean startsWith(SegmentsPath prefix) {
        return id.startsWith(prefix.id);
    }


    @Override public @Nullable SegmentsPath getParent() {
        final @Nullable SegmentsIdentifier parent = id.getParent();
        if(parent == null) return null;
        return new SegmentsPath(qualifier, parent);
    }

    @Override public @Nullable SegmentsPath getRoot() {
        final @Nullable SegmentsIdentifier root = id.getRoot();
        if(root == null) return null;
        return new SegmentsPath(qualifier, root);
    }

    @Override public @Nullable String getLeaf() {
        return id.getLeaf();
    }


    @Override public SegmentsPath getNormalized() {
        return new SegmentsPath(qualifier, id.getNormalized());
    }


    @Override public String relativize(ResourcePath other) {
        if(!(other instanceof SegmentsPath)) {
            throw new ResourceRuntimeException("Cannot relativize with path '" + other + "', it is not a SegmentsPath");
        }
        return relativize((SegmentsPath)other);
    }

    public String relativize(SegmentsPath other) {
        return id.relativize(other.id).toString();
    }


    @Override public SegmentsPath appendSegment(String segment) {
        return new SegmentsPath(qualifier, id.appendSegment(segment));
    }

    @Override public SegmentsPath appendSegments(Iterable<String> segments) {
        return new SegmentsPath(qualifier, id.appendSegments(segments));
    }

    @Override public SegmentsPath appendSegments(Collection<String> segments) {
        return new SegmentsPath(qualifier, id.appendSegments(segments));
    }


    @Override public SegmentsPath appendRelativePath(String relativePath) {
        return new SegmentsPath(qualifier, id.appendRelativePath(relativePath));
    }

    @Override public SegmentsPath appendOrReplaceWithPath(String other) {
        return new SegmentsPath(qualifier, id.appendOrReplaceWithPath(other));
    }


    @Override public SegmentsPath appendRelativePath(ResourcePath relativePath) {
        if(!(relativePath instanceof SegmentsPath)) {
            throw new ResourceRuntimeException("Cannot append '" + relativePath + "', it is not a SegmentsPath");
        }
        return appendRelativePath((SegmentsPath)relativePath);
    }

    public SegmentsPath appendRelativePath(SegmentsPath relativePath) {
        return new SegmentsPath(qualifier, id.appendRelativePath(relativePath.id));
    }


    @Override public SegmentsPath replaceLeaf(String segment) {
        return new SegmentsPath(qualifier, id.replaceLeaf(segment));
    }


    @Override protected SegmentsPath self() {
        return this;
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final SegmentsPath that = (SegmentsPath)o;
        return qualifier.equals(that.qualifier) && id.equals(that.id);
    }

    @Override public int hashCode() {
        return Objects.hash(qualifier, id);
    }

    @Override public String toString() {
        return asString();
    }
}
