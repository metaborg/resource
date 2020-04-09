package mb.resource.classloader;

import mb.resource.ResourceRuntimeException;
import mb.resource.hierarchical.PathNormalizerUtil;
import mb.resource.hierarchical.ResourcePath;
import mb.resource.hierarchical.ResourcePathDefaults;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ClassLoaderResourcePath extends ResourcePathDefaults<ClassLoaderResourcePath> implements ResourcePath {
    public static class Identifier implements Serializable {
        private final @Nullable String root;
        private final List<String> segments;

        private Identifier(@Nullable String root, List<String> segments) {
            this.root = root;
            this.segments = segments;
        }

        private Identifier(List<String> segments) {
            this(null, segments);
        }

        private Identifier(String root) {
            this(root, new ArrayList<>());
        }

        private static Identifier fromString(String str) {
            final String[] segments = str.split("/");
            final @Nullable String root;
            if(str.startsWith("/")) {
                root = "/";
            } else {
                root = null;
            }
            return new Identifier(root, Arrays.asList(segments));
        }


        private boolean isAbsolute() {
            return root != null;
        }


        private @Nullable Identifier getParent() {
            final int size = segments.size();
            if(size > 1) {
                return new Identifier(root, new ArrayList<>(segments.subList(0, size - 1)));
            }
            if(size > 0 && root != null) {
                return new Identifier(root);
            }
            return null;
        }

        private @Nullable Identifier getRoot() {
            if(root != null) {
                return new Identifier(root);
            }
            return null;
        }

        private @Nullable String getLeaf() {
            final int size = segments.size();
            if(size > 0) {
                return segments.get(size - 1);
            }
            // TODO: do we want to return the root as a leaf, if there are no segments?
            return root; // When there are no segments, and root is null, null is returned to indicate there is no leaf.
        }


        private Identifier getNormalized() {
            final ArrayList<String> newSegments = PathNormalizerUtil.normalize(segments, segments.size());
            return new Identifier(root, newSegments);
        }


        private Identifier relativize(Identifier other) {
            final int segmentsSize = segments.size();
            if(segmentsSize > other.segments.size()) {
                throw new ResourceRuntimeException("Cannot relativize path '" + other + "' to this path '" + this + "', this path has more segments");
            }
            for(int i = 0; i < segmentsSize; i++) {
                if(!segments.get(i).equals(other.segments.get(i))) {
                    throw new ResourceRuntimeException("Cannot relativize path '" + other + "' to this path '" + this + "', there is no common root");
                }
            }
            return new Identifier(root, new ArrayList<>(other.segments.subList(segmentsSize, other.segments.size()))); // TODO: TEST!
        }


        public Identifier appendSegment(String segment) {
            final ArrayList<String> newSegments = new ArrayList<>(this.segments.size() + 1);
            newSegments.addAll(this.segments);
            newSegments.add(segment);
            return new Identifier(root, newSegments);
        }

        public Identifier appendSegments(Iterable<String> segments) {
            final ArrayList<String> newSegments = new ArrayList<>(this.segments);
            segments.forEach(newSegments::add);
            return new Identifier(root, newSegments);
        }

        public Identifier appendSegments(Collection<String> segments) {
            final ArrayList<String> newSegments = new ArrayList<>(this.segments.size() + segments.size());
            newSegments.addAll(this.segments);
            newSegments.addAll(segments);
            return new Identifier(root, newSegments);
        }

        public Identifier appendSegments(String... segments) {
            return appendSegments(Arrays.asList(segments));
        }


        public Identifier appendRelativePath(String relativePath) {
            if(relativePath.startsWith("/")) {
                throw new ResourceRuntimeException("Cannot append '" + relativePath + "', it is an absolute path");
            }
            final String[] relativePathSegments = relativePath.split("/");
            return appendSegments(relativePathSegments); // TODO: TEST
        }

        public Identifier appendOrReplaceWithPath(String other) {
            final String[] relativePathSegments = other.split("/");
            if(other.startsWith("/")) {
                return new Identifier("/", Arrays.asList(relativePathSegments));
            }
            return appendSegments(relativePathSegments); // TODO: TEST
        }

        public Identifier appendString(String other) {
            final String[] pathSegments = other.split("/");
            return appendSegments(pathSegments); // TODO: TEST
        }


        public Identifier appendRelativePath(Identifier relativePath) {
            if(relativePath.isAbsolute()) {
                throw new ResourceRuntimeException("Cannot append '" + relativePath + "', it is an absolute path");
            }
            return appendSegments(relativePath.segments);
        }

        public Identifier appendOrReplaceWithPath(Identifier other) {
            if(other.isAbsolute()) {
                return other;
            }
            return appendSegments(other.segments);
        }


        public Identifier replaceLeaf(String segment) {
            final int size = segments.size();
            if(size > 0) {
                final ArrayList<String> newSegments = new ArrayList<>(segments);
                newSegments.set(size - 1, segment);
                return new Identifier(root, newSegments);
            }
            if(root != null) {
                // TODO: do we want to replace the root as a leaf, if there are no segments?
                return new Identifier(segment);
            }
            return this;
        }


        @Override public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            final Identifier that = (Identifier)o;
            return Objects.equals(root, that.root) && segments.equals(that.segments);
        }

        @Override public int hashCode() {
            return Objects.hash(root, segments);
        }

        @Override public String toString() {
            return (root != null ? root : "") + String.join("/", segments);
        }
    }

    private final String qualifier;
    private final Identifier id;

    ClassLoaderResourcePath(String qualifier, Identifier id) {
        this.qualifier = qualifier;
        this.id = id;
    }

    ClassLoaderResourcePath(String qualifier, String id) {
        this.qualifier = qualifier;
        this.id = Identifier.fromString(id);
    }


    @Override public String getQualifier() {
        return qualifier;
    }

    @Override public Identifier getId() {
        return id;
    }


    @Override public boolean isAbsolute() {
        return id.isAbsolute();
    }


    @Override public int getSegmentCount() {
        return id.segments.size();
    }

    @Override public Iterable<String> getSegments() {
        return id.segments;
    }

    @Override public String asString() {
        return id.toString();
    }


    @Override public @Nullable ClassLoaderResourcePath getParent() {
        final @Nullable Identifier parent = id.getParent();
        if(parent == null) return null;
        return new ClassLoaderResourcePath(qualifier, parent);
    }

    @Override public @Nullable ClassLoaderResourcePath getRoot() {
        final @Nullable Identifier root = id.getRoot();
        if(root == null) return null;
        return new ClassLoaderResourcePath(qualifier, root);
    }

    @Override public @Nullable String getLeaf() {
        return id.getLeaf();
    }


    @Override public ClassLoaderResourcePath getNormalized() {
        return new ClassLoaderResourcePath(qualifier, id.getNormalized());
    }


    @Override public ClassLoaderResourcePath relativize(ResourcePath other) {
        if(!(other instanceof ClassLoaderResourcePath)) {
            throw new ResourceRuntimeException("Cannot relativize with path '" + other + "', it is not a ClassLoaderResourcePath");
        }
        return relativize((ClassLoaderResourcePath)other);
    }

    public ClassLoaderResourcePath relativize(ClassLoaderResourcePath other) {
        return new ClassLoaderResourcePath(qualifier, id.relativize(other.id));
    }

    @Override public String relativizeToString(ResourcePath other) {
        if(!(other instanceof ClassLoaderResourcePath)) {
            throw new ResourceRuntimeException("Cannot relativize with path '" + other + "', it is not a ClassLoaderResourcePath");
        }
        return relativizeToString((ClassLoaderResourcePath)other);
    }

    public String relativizeToString(ClassLoaderResourcePath other) {
        return id.relativize(other.id).toString();
    }


    @Override public ClassLoaderResourcePath appendSegment(String segment) {
        return new ClassLoaderResourcePath(qualifier, id.appendSegment(segment));
    }

    @Override public ClassLoaderResourcePath appendSegments(Iterable<String> segments) {
        return new ClassLoaderResourcePath(qualifier, id.appendSegments(segments));
    }

    @Override public ClassLoaderResourcePath appendSegments(Collection<String> segments) {
        return new ClassLoaderResourcePath(qualifier, id.appendSegments(segments));
    }


    @Override public ClassLoaderResourcePath appendRelativePath(String relativePath) {
        return new ClassLoaderResourcePath(qualifier, id.appendRelativePath(relativePath));
    }

    @Override public ClassLoaderResourcePath appendOrReplaceWithPath(String other) {
        return new ClassLoaderResourcePath(qualifier, id.appendOrReplaceWithPath(other));
    }

    @Override public ClassLoaderResourcePath appendString(String other) {
        return new ClassLoaderResourcePath(qualifier, id.appendString(other));
    }


    @Override public ClassLoaderResourcePath appendRelativePath(ResourcePath relativePath) {
        if(!(relativePath instanceof ClassLoaderResourcePath)) {
            throw new ResourceRuntimeException("Cannot append '" + relativePath + "', it is not a ClassLoaderResourcePath");
        }
        return appendRelativePath((ClassLoaderResourcePath)relativePath);
    }

    public ClassLoaderResourcePath appendRelativePath(ClassLoaderResourcePath relativePath) {
        return new ClassLoaderResourcePath(qualifier, id.appendRelativePath(relativePath.id));
    }


    @Override public ClassLoaderResourcePath replaceLeaf(String segment) {
        return new ClassLoaderResourcePath(qualifier, id.replaceLeaf(segment));
    }


    @Override protected ClassLoaderResourcePath self() {
        return this;
    }


    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ClassLoaderResourcePath that = (ClassLoaderResourcePath)o;
        return qualifier.equals(that.qualifier) && id.equals(that.id);
    }

    @Override public int hashCode() {
        return Objects.hash(qualifier, id);
    }
}
