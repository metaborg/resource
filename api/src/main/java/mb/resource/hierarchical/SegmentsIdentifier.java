package mb.resource.hierarchical;

import mb.resource.ResourceRuntimeException;
import mb.resource.util.SeparatorUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SegmentsIdentifier implements Serializable {
    private final @Nullable String root;
    private final List<String> segments;

    public SegmentsIdentifier(@Nullable String root, List<String> segments) {
        this.root = root;
        this.segments = segments;
    }

    public SegmentsIdentifier(List<String> segments) {
        this(null, segments);
    }

    public SegmentsIdentifier(String root) {
        this(root, new ArrayList<>());
    }

    public static SegmentsIdentifier fromString(String str) {
        final String[] segments = SeparatorUtil.splitWithUnixSeparator(str);
        final @Nullable String root;
        if(SeparatorUtil.startsWithUnixSeparator(str)) {
            root = SeparatorUtil.unixSeparator;
        } else {
            root = null;
        }
        return new SegmentsIdentifier(root, Arrays.asList(segments));
    }


    public boolean isAbsolute() {
        return root != null;
    }


    public int getSegmentCount() {
        return segments.size();
    }

    public Iterable<String> getSegments() {
        return segments;
    }


    public boolean startsWith(SegmentsIdentifier prefix) {
        if(segments.size() < prefix.segments.size()) return false;
        for(int i = 0; i < prefix.segments.size(); i++) {
            if(!prefix.segments.get(i).equals(segments.get(i))) return false;
        }
        return true;
    }


    public @Nullable SegmentsIdentifier getParent() {
        final int size = segments.size();
        if(size > 1) {
            return new SegmentsIdentifier(root, new ArrayList<>(segments.subList(0, size - 1)));
        }
        if(size > 0 && root != null) {
            return new SegmentsIdentifier(root);
        }
        return null;
    }

    public @Nullable SegmentsIdentifier getRoot() {
        if(root != null) {
            return new SegmentsIdentifier(root);
        }
        return null;
    }

    public @Nullable String getLeaf() {
        final int size = segments.size();
        if(size > 0) {
            return segments.get(size - 1);
        }
        // TODO: do we want to return the root as a leaf, if there are no segments?
        return root; // When there are no segments, and root is null, null is returned to indicate there is no leaf.
    }


    public SegmentsIdentifier getNormalized() {
        final ArrayList<String> newSegments = PathNormalizerUtil.normalize(segments, segments.size());
        return new SegmentsIdentifier(root, newSegments);
    }


    public SegmentsIdentifier relativize(SegmentsIdentifier other) {
        final int segmentsSize = segments.size();
        if(segmentsSize > other.segments.size()) {
            throw new ResourceRuntimeException("Cannot relativize path '" + other + "' to this path '" + this + "', this path has more segments");
        }
        for(int i = 0; i < segmentsSize; i++) {
            if(!segments.get(i).equals(other.segments.get(i))) {
                throw new ResourceRuntimeException("Cannot relativize path '" + other + "' to this path '" + this + "', there is no common root");
            }
        }
        return new SegmentsIdentifier(root, new ArrayList<>(other.segments.subList(segmentsSize, other.segments.size()))); // TODO: TEST!
    }


    public SegmentsIdentifier appendSegment(String segment) {
        final ArrayList<String> newSegments = new ArrayList<>(this.segments.size() + 1);
        newSegments.addAll(this.segments);
        newSegments.add(segment);
        return new SegmentsIdentifier(root, newSegments);
    }

    public SegmentsIdentifier appendSegments(Iterable<String> segments) {
        final ArrayList<String> newSegments = new ArrayList<>(this.segments);
        segments.forEach(newSegments::add);
        return new SegmentsIdentifier(root, newSegments);
    }

    public SegmentsIdentifier appendSegments(Collection<String> segments) {
        final ArrayList<String> newSegments = new ArrayList<>(this.segments.size() + segments.size());
        newSegments.addAll(this.segments);
        newSegments.addAll(segments);
        return new SegmentsIdentifier(root, newSegments);
    }

    public SegmentsIdentifier appendSegments(String... segments) {
        return appendSegments(Arrays.asList(segments));
    }


    public SegmentsIdentifier appendAsRelativePath(String path) {
        final String[] relativePathSegments = SeparatorUtil.splitWithUnixSeparator(path);
        return appendSegments(relativePathSegments); // TODO: TEST
    }

    public SegmentsIdentifier appendRelativePath(String relativePath) {
        if(SeparatorUtil.startsWithUnixSeparator(relativePath)) {
            throw new ResourceRuntimeException("Cannot append '" + relativePath + "', it is an absolute path");
        }
        final String[] relativePathSegments = SeparatorUtil.splitWithUnixSeparator(relativePath);
        return appendSegments(relativePathSegments); // TODO: TEST
    }

    public SegmentsIdentifier appendOrReplaceWithPath(String other) {
        final String[] relativePathSegments = SeparatorUtil.splitWithUnixSeparator(other);
        if(SeparatorUtil.startsWithUnixSeparator(other)) {
            return new SegmentsIdentifier(SeparatorUtil.unixSeparator, Arrays.asList(relativePathSegments));
        }
        return appendSegments(relativePathSegments); // TODO: TEST
    }

    public SegmentsIdentifier appendString(String other) {
        final String[] pathSegments = SeparatorUtil.splitWithUnixSeparator(other);
        return appendSegments(pathSegments); // TODO: TEST
    }


    public SegmentsIdentifier appendRelativePath(SegmentsIdentifier relativePath) {
        if(relativePath.isAbsolute()) {
            throw new ResourceRuntimeException("Cannot append '" + relativePath + "', it is an absolute path");
        }
        return appendSegments(relativePath.segments);
    }

    public SegmentsIdentifier appendOrReplaceWithPath(SegmentsIdentifier other) {
        if(other.isAbsolute()) {
            return other;
        }
        return appendSegments(other.segments);
    }


    public SegmentsIdentifier replaceLeaf(String segment) {
        final int size = segments.size();
        if(size > 0) {
            final ArrayList<String> newSegments = new ArrayList<>(segments);
            newSegments.set(size - 1, segment);
            return new SegmentsIdentifier(root, newSegments);
        }
        if(root != null) {
            // TODO: do we want to replace the root as a leaf, if there are no segments?
            return new SegmentsIdentifier(segment);
        }
        return this;
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final SegmentsIdentifier that = (SegmentsIdentifier)o;
        return Objects.equals(root, that.root) && segments.equals(that.segments);
    }

    @Override public int hashCode() {
        return Objects.hash(root, segments);
    }

    @Override public String toString() {
        return (root != null ? root : "") + SeparatorUtil.joinWithUnixSeparator(segments);
    }
}
