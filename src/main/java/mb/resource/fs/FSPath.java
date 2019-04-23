package mb.resource.fs;

import mb.resource.ResourceKey;
import mb.resource.ResourceRuntimeException;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class FSPath implements ResourceKey, Comparable<FSPath>, Serializable {
    static final String qualifier = "java";

    // URI version of the path which can be serialized and deserialized.
    final URI uri;
    // Transient and non-final for deserialization in readObject. Invariant: always nonnull.
    transient Path javaPath;


    public FSPath(Path javaPath) {
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
     * @return {@link Path} corresponding to this path.
     */
    public Path getJavaPath() {
        return javaPath;
    }

    /**
     * @return {@link URI} corresponding to this path.
     */
    public URI getURI() {
        return uri;
    }

    /**
     * @return true if this path is a local file system path, false otherwise.
     */
    public boolean isLocalPath() {
        return javaPath.getFileSystem().equals(FileSystems.getDefault());
    }


    public boolean isAbsolute() {
        return javaPath.isAbsolute();
    }

    /**
     * @return this path if it {@link #isAbsolute()}, or returns an absolute path by appending this path to {@link #workingDirectory()}.
     */
    public FSPath toAbsoluteFromWorkingDirectory() {
        if(javaPath.isAbsolute()) {
            return this;
        } else {
            return workingDirectory().appendRelativePath(this);
        }
    }

    /**
     * @return this path if it {@link #isAbsolute()}, or returns an absolute path by appending this path to {@link #homeDirectory()}.
     */
    public FSPath toAbsoluteFromHomeDirectory() {
        if(javaPath.isAbsolute()) {
            return this;
        } else {
            return homeDirectory().appendRelativePath(this);
        }
    }


    public int getSegmentCount() {
        return javaPath.getNameCount();
    }

    public Iterable<String> getSegments() {
        return () -> new PathIterator(javaPath.iterator());
    }


    public @Nullable FSPath getParent() {
        final @Nullable Path parentJavaPath = this.javaPath.getParent();
        if(parentJavaPath == null) {
            return null;
        }
        return new FSPath(parentJavaPath);
    }

    public @Nullable FSPath getRoot() {
        final @Nullable Path rootJavaPath = this.javaPath.getRoot();
        if(rootJavaPath == null) {
            return null;
        }
        return new FSPath(rootJavaPath);
    }

    public @Nullable String getLeaf() {
        final @Nullable Path fileName = this.javaPath.getFileName();
        if(fileName == null) {
            return null;
        }
        return fileName.toString();
    }

    public @Nullable String getLeafExtension() {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return null;
        }
        return FilenameExtensionUtil.extension(leaf);
    }

    public FSPath getNormalized() {
        final Path normalizedJavaPath = this.javaPath.normalize();
        return new FSPath(normalizedJavaPath);
    }

    public FSPath relativize(FSPath other) {
        final Path javaRelativePath = javaPath.relativize(other.javaPath);
        return new FSPath(javaRelativePath);
    }


    public FSPath appendSegment(String segment) {
        final Path javaPath = this.javaPath.resolve(segment);
        return new FSPath(javaPath);
    }

    public FSPath appendSegments(Iterable<String> segments) {
        final ArrayList<String> segmentsList = new ArrayList<>();
        segments.forEach(segmentsList::add);
        return appendSegments(segmentsList);
    }

    public FSPath appendSegments(Collection<String> segments) {
        final Path relJavaPath = createLocalPath(segments);
        final Path javaPath = this.javaPath.resolve(relJavaPath);
        return new FSPath(javaPath);
    }

    public FSPath appendSegments(List<String> segments) {
        final Path relJavaPath = createLocalPath(segments);
        final Path javaPath = this.javaPath.resolve(relJavaPath);
        return new FSPath(javaPath);
    }

    public FSPath appendSegments(String... segments) {
        final Path relJavaPath = createLocalPath(segments);
        final Path javaPath = this.javaPath.resolve(relJavaPath);
        return new FSPath(javaPath);
    }


    /**
     * @throws ResourceRuntimeException when relativePath is not a relative path (but instead an absolute one).
     */
    public FSPath appendRelativePath(FSPath relativePath) {
        if(relativePath.isAbsolute()) {
            throw new ResourceRuntimeException(
                "Cannot append path '" + relativePath + "', it is an absolute path");
        }
        final Path javaPath = this.javaPath.resolve(relativePath.javaPath);
        return new FSPath(javaPath);
    }

    public FSPath appendJavaPath(Path segments) {
        final Path javaPath = this.javaPath.resolve(segments);
        return new FSPath(javaPath);
    }


    public FSPath appendToLeaf(String str) {
        final String fileName = this.javaPath.getFileName().toString();
        final String newFileName = fileName + str;
        final Path javaPath = this.javaPath.resolveSibling(newFileName);
        return new FSPath(javaPath);
    }

    public FSPath replaceLeaf(String segment) {
        final Path javaPath = this.javaPath.resolveSibling(segment);
        return new FSPath(javaPath);
    }

    public FSPath applyToLeaf(Function<String, String> func) {
        final String fileName = this.javaPath.getFileName().toString();
        final Path javaPath = this.javaPath.resolveSibling(func.apply(fileName));
        return new FSPath(javaPath);
    }

    public FSPath replaceLeafExtension(String extension) {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return this;
        }
        return replaceLeaf(FilenameExtensionUtil.replaceExtension(leaf, extension));
    }

    public FSPath appendExtensionToLeaf(String extension) {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return this;
        }
        return replaceLeaf(FilenameExtensionUtil.appendExtension(leaf, extension));
    }

    public FSPath applyToLeafExtension(Function<String, String> func) {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return this;
        }
        return replaceLeaf(FilenameExtensionUtil.applyToExtension(leaf, func));
    }


    private static Path createJavaPath(URI uri) {
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


    /**
     * @return "java" as the {@link ResourceKey} qualifier, indicating it belongs to the java.nio.file filesystem.
     */
    @Override public String qualifier() {
        return qualifier;
    }

    /**
     * @return this path as the {@link ResourceKey} identifier.
     */
    @Override public FSPath id() {
        return this;
    }


    @Override public int compareTo(FSPath other) {
        return this.javaPath.compareTo(other.javaPath);
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final FSPath javaPath = (FSPath) o;
        return uri.equals(javaPath.uri);
    }

    @Override public int hashCode() {
        return uri.hashCode();
    }

    @Override public String toString() {
        return javaPath.toString();
    }


    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        this.javaPath = createJavaPath(this.uri);
    }
}
