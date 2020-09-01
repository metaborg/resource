package mb.resource.fs;

import mb.resource.ResourceRuntimeException;
import mb.resource.hierarchical.ResourcePath;
import mb.resource.hierarchical.ResourcePathDefaults;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

public class FSPath extends ResourcePathDefaults<FSPath> implements ResourcePath, Comparable<FSPath>, Serializable {
    // URI version of the path which can be serialized and deserialized.
    final URI uri;
    // Transient and non-final for deserialization in readObject. Invariant: always nonnull.
    transient Path javaPath;


    public FSPath(Path javaPath) {
        /* TODO: many Path/Filesystem implementations convert the path to a path that is absolute to some random-ass
                 directory, before turning it into a URI. Consequently, the URI is out of sync with the actual path when
                 the path is relative. Therefore, we should probably disallow relative paths for FSPath, or remove all
                 relative path API from ResourcePath, and instead only use strings for relative paths. Alternatively,
                 there may be another way to create URIs?
         */
        this.uri = javaPath.toUri();
        this.javaPath = javaPath;
    }

    public FSPath(URI uri) {
        this.uri = uri;
        this.javaPath = createJavaPath(uri);
    }

    public FSPath(File javaFile) {
        this(createLocalPath(javaFile));
    }

    public FSPath(String localPathStr) {
        this(createLocalPath(localPathStr));
    }


    /**
     * @return absolute path of the current working directory (given by {@code System.getProperty("user.dir")}.
     */
    public static FSPath workingDirectory() {
        return new FSPath(System.getProperty("user.dir"));
    }

    /**
     * @return absolute path of the current user's home directory (given by {@code System.getProperty("user.home")}.
     */
    public static FSPath homeDirectory() {
        return new FSPath(System.getProperty("user.home"));
    }

    /**
     * @return absolute path of the system's temporary directory (given by {@code System.getProperty("java.io.tmpdir")}.
     */
    public static FSPath temporaryDirectory() {
        return new FSPath(System.getProperty("java.io.tmpdir"));
    }


    public Path getJavaPath() {
        return javaPath;
    }

    public URI getURI() {
        return uri;
    }


    /**
     * @return true if this path is a local file system path, false otherwise.
     */
    public boolean isLocalPath() {
        return javaPath.getFileSystem().equals(FileSystems.getDefault());
    }


    @Override public boolean isAbsolute() {
        return javaPath.isAbsolute();
    }

    /**
     * @return this path if it {@link #isAbsolute()}, or returns an absolute path by appending this path to {@link
     * #workingDirectory()}.
     */
    public FSPath toAbsoluteFromWorkingDirectory() {
        if(javaPath.isAbsolute()) {
            return this;
        } else {
            return workingDirectory().appendRelativePath(this);
        }
    }

    /**
     * @return this path if it {@link #isAbsolute()}, or returns an absolute path by appending this path to {@link
     * #homeDirectory()}.
     */
    public FSPath toAbsoluteFromHomeDirectory() {
        if(javaPath.isAbsolute()) {
            return this;
        } else {
            return homeDirectory().appendRelativePath(this);
        }
    }


    @Override public int getSegmentCount() {
        return javaPath.getNameCount();
    }

    @Override public Iterable<String> getSegments() {
        return () -> new PathIterator(javaPath.iterator());
    }


    @Override public boolean startsWith(ResourcePath prefix) {
        if(!(prefix instanceof FSPath)) {
            throw new ResourceRuntimeException("Cannot check if this path starts with '" + prefix + "', it is not an FSPath");
        }
        return startsWith((FSPath)prefix);
    }

    public boolean startsWith(FSPath prefix) {
        return javaPath.startsWith(prefix.javaPath);
    }


    @Override public @Nullable FSPath getParent() {
        final @Nullable Path parentJavaPath = this.javaPath.getParent();
        if(parentJavaPath == null) {
            return null;
        }
        return new FSPath(parentJavaPath);
    }

    @Override public @Nullable FSPath getRoot() {
        final @Nullable Path rootJavaPath = this.javaPath.getRoot();
        if(rootJavaPath == null) {
            return null;
        }
        return new FSPath(rootJavaPath);
    }

    @Override public @Nullable String getLeaf() {
        final @Nullable Path fileName = this.javaPath.getFileName();
        if(fileName == null) {
            return null;
        }
        return fileName.toString();
    }


    @Override public FSPath getNormalized() {
        final Path normalizedJavaPath = this.javaPath.normalize();
        return new FSPath(normalizedJavaPath);
    }


    @Override public String relativize(ResourcePath other) {
        if(!(other instanceof FSPath)) {
            throw new ResourceRuntimeException("Cannot relativize against '" + other + "', it is not an FSPath");
        }
        return relativize((FSPath)other);
    }

    public String relativize(FSPath other) {
        return javaPath.relativize(other.javaPath).toString();
    }


    @Override public FSPath appendSegment(String segment) {
        final Path javaPath = this.javaPath.resolve(segment);
        return new FSPath(javaPath);
    }

    @Override public FSPath appendSegments(Iterable<String> segments) {
        final ArrayList<String> segmentsList = new ArrayList<>();
        segments.forEach(segmentsList::add);
        return appendSegments(segmentsList);
    }

    @Override public FSPath appendSegments(Collection<String> segments) {
        final Path relJavaPath = createLocalPath(segments);
        final Path javaPath = this.javaPath.resolve(relJavaPath);
        return new FSPath(javaPath);
    }


    @Override public FSPath appendRelativePath(String relativePath) {
        final Path other = javaPath.getFileSystem().getPath(relativePath);
        if(other.isAbsolute()) {
            throw new ResourceRuntimeException(
                "Cannot append path '" + relativePath + "', it is an absolute path");
        }
        return appendOrReplaceWithPath(other);
    }

    @Override public FSPath appendOrReplaceWithPath(String other) {
        return appendOrReplaceWithPath(javaPath.getFileSystem().getPath(other));
    }

    @Override public FSPath appendString(String other) {
        final String appended = uri.toString() + other;
        try {
            final URI appendedUri = new URI(appended);
            return new FSPath(appendedUri);
        } catch(URISyntaxException e) {
            throw new ResourceRuntimeException("Cannot append string '" + other + "' to '" + uri + "'", e);
        }
    }

    @Override public FSPath appendRelativePath(ResourcePath relativePath) {
        if(!(relativePath instanceof FSPath)) {
            throw new ResourceRuntimeException("Cannot append '" + relativePath + "', it is not an FSPath");
        }
        return appendRelativePath((FSPath)relativePath);
    }

    public FSPath appendRelativePath(FSPath relativePath) {
        if(relativePath.isAbsolute()) {
            throw new ResourceRuntimeException(
                "Cannot append path '" + relativePath + "', it is an absolute path");
        }
        final Path javaPath = this.javaPath.resolve(relativePath.javaPath);
        return new FSPath(javaPath);
    }

    @Override public FSPath appendOrReplaceWithPath(ResourcePath other) {
        if(!(other instanceof FSPath)) {
            throw new ResourceRuntimeException("Cannot append or replace from '" + other + "', it is not an FSPath");
        }
        return appendOrReplaceWithPath((FSPath)other);
    }

    public FSPath appendOrReplaceWithPath(FSPath other) {
        final Path javaPath = this.javaPath.resolve(other.javaPath);
        return new FSPath(javaPath);
    }

    public FSPath appendOrReplaceWithPath(Path other) {
        final Path javaPath = this.javaPath.resolve(other);
        return new FSPath(javaPath);
    }


    @Override public FSPath replaceLeaf(String segment) {
        final Path javaPath = this.javaPath.resolveSibling(segment);
        return new FSPath(javaPath);
    }


    private static Path createJavaPath(URI uri) {
        if(uri.getScheme() == null) {
            // If the URI schema is null, Paths.get(uri) won't work. Just create a local path from the string of the URI instead.
            return createLocalPath(uri.toString());
        }
        try {
            return Paths.get(uri);
        } catch(IllegalArgumentException | FileSystemNotFoundException e) {
            throw new ResourceRuntimeException("Creating Java path from URI '" + uri + "' failed unexpectedly", e);
        }
    }

    private static Path createLocalPath(Collection<String> segments) {
        final int segmentsSize = segments.size();
        if(segmentsSize == 0) {
            return FileSystems.getDefault().getPath("/");
        } else {
            @Nullable String first = null;
            final String[] more = new String[segmentsSize - 1];
            int i = 0;
            for(String segment : segments) {
                if(first == null) {
                    first = segment;
                } else {
                    more[++i] = segment;
                }
            }
            return FileSystems.getDefault().getPath(first, more);
        }
    }

    private static Path createLocalPath(String... segments) {
        final int segmentsSize = segments.length;
        if(segmentsSize == 0) {
            return FileSystems.getDefault().getPath("/");
        } else {
            final String first = segments[0];
            final String[] more = new String[segmentsSize - 1];
            System.arraycopy(segments, 1, more, 0, segmentsSize - 1);
            return FileSystems.getDefault().getPath(first, more);
        }
    }

    private static Path createLocalPath(File javaFile) {
        try {
            return FileSystems.getDefault().getPath(javaFile.getPath());
        } catch(InvalidPathException e) {
            throw new ResourceRuntimeException("Creating local path from file '" + javaFile + "' failed unexpectedly",
                e);
        }
    }

    private static Path createLocalPath(String localPathStr) {
        try {
            return FileSystems.getDefault().getPath(localPathStr);
        } catch(InvalidPathException e) {
            throw new ResourceRuntimeException(
                "Creating local path from string '" + localPathStr + "' failed unexpectedly", e);
        }
    }


    @Override public String getQualifier() {
        return FSResourceRegistry.qualifier;
    }

    @Override public URI getId() {
        return uri;
    }

    @Override public String getIdAsString() {
        return uri.toString();
    }


    @Override public int compareTo(FSPath other) {
        return this.javaPath.compareTo(other.javaPath);
    }


    @Override protected FSPath self() {
        return this;
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final FSPath javaPath = (FSPath)o;
        return uri.equals(javaPath.uri);
    }

    @Override public int hashCode() {
        return uri.hashCode();
    }

    @Override public String toString() {
        return asString();
    }


    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        this.javaPath = createJavaPath(this.uri);
    }
}
