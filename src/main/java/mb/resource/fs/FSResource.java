package mb.resource.fs;

import mb.resource.ReadableResource;
import mb.resource.Resource;
import mb.resource.ResourceRuntimeException;
import mb.resource.WritableResource;
import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.HierarchicalResourceAccess;
import mb.resource.hierarchical.HierarchicalResourceType;
import mb.resource.hierarchical.ResourcePath;
import mb.resource.hierarchical.match.ResourceMatcher;
import mb.resource.hierarchical.walk.ResourceWalker;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class FSResource implements Resource, ReadableResource, WritableResource, HierarchicalResource, Serializable {
    final FSPath path;


    public FSResource(FSPath path) {
        this.path = path;
    }

    public FSResource(java.nio.file.Path javaPath) {
        this.path = new FSPath(javaPath);
    }

    public FSResource(URI uri) {
        this.path = new FSPath(uri);
    }

    public FSResource(File file) {
        this.path = new FSPath(file);
    }

    public FSResource(String localPathStr) {
        this.path = new FSPath(localPathStr);
    }


    /**
     * Creates a resource for the current working directory.
     */
    public static FSResource workingDirectory() {
        return new FSResource(FSPath.workingDirectory());
    }

    /**
     * Creates a resource for the user's home directory.
     */
    public static FSResource homeDirectory() {
        return new FSResource(FSPath.homeDirectory());
    }

    /**
     * Creates a resource for the system's temporary directory.
     */
    public static FSResource temporaryDirectory() {
        return new FSResource(FSPath.temporaryDirectory());
    }


    /**
     * Creates a temporary directory with given {@code prefix}.
     */
    public static FSResource createTemporaryDirectory(String prefix) throws IOException {
        return new FSResource(Files.createTempDirectory(prefix));
    }

    /**
     * Creates a temporary directory inside given {@code directory} with given {@code prefix}.
     */
    public static FSResource createTemporaryDirectory(FSResource directory, String prefix) throws IOException {
        return new FSResource(Files.createTempDirectory(directory.path.javaPath, prefix));
    }

    /**
     * Creates a temporary file with given {@code prefix} and {@code suffix}.
     */
    public static FSResource createTemporaryFile(String prefix, String suffix) throws IOException {
        return new FSResource(Files.createTempFile(prefix, suffix));
    }

    /**
     * Creates a temporary file inside given {@code directory} with given {@code prefix} and {@code suffix}.
     */
    public static FSResource createTemporaryFile(FSResource directory, String prefix, String suffix) throws IOException {
        return new FSResource(Files.createTempFile(directory.path.javaPath, prefix, suffix));
    }


    @Override public FSPath getPath() {
        return path;
    }

    @Override public FSPath getKey() {
        return path;
    }


    public java.nio.file.Path getJavaPath() {
        return path.javaPath;
    }

    public URI getURI() {
        return path.uri;
    }

    public boolean isLocalPath() {
        return path.javaPath.getFileSystem().equals(FileSystems.getDefault());
    }


    @Override public @Nullable FSResource getParent() {
        final @Nullable FSPath newPath = path.getParent();
        if(newPath == null) {
            return null;
        }
        return new FSResource(newPath);
    }

    @Override public @Nullable FSResource getRoot() {
        final @Nullable FSPath newPath = path.getRoot();
        if(newPath == null) {
            return null;
        }
        return new FSResource(newPath);
    }

    @Override public @Nullable String getLeaf() {
        return path.getLeaf();
    }

    @Override public @Nullable String getLeafExtension() {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return null;
        }
        return FilenameExtensionUtil.getExtension(leaf);
    }


    @Override public FSResource appendSegment(String segment) {
        final FSPath newPath = path.appendSegment(segment);
        return new FSResource(newPath);
    }

    @Override public FSResource appendSegments(Iterable<String> segments) {
        final FSPath newPath = path.appendSegments(segments);
        return new FSResource(newPath);
    }

    @Override public FSResource appendSegments(Collection<String> segments) {
        final FSPath newPath = path.appendSegments(segments);
        return new FSResource(newPath);
    }

    @Override public FSResource appendSegments(List<String> segments) {
        final FSPath newPath = path.appendSegments(segments);
        return new FSResource(newPath);
    }

    @Override public FSResource appendSegments(String... segments) {
        final FSPath newPath = path.appendSegments(segments);
        return new FSResource(newPath);
    }


    @Override public HierarchicalResource appendRelativePath(String relativePath) {
        final FSPath newPath = path.appendRelativePath(relativePath);
        return new FSResource(newPath);
    }

    @Override public HierarchicalResource appendOrReplaceWithPath(String other) {
        final FSPath newPath = path.appendOrReplaceWithPath(other);
        return new FSResource(newPath);
    }

    @Override public FSResource appendRelativePath(ResourcePath relativePath) {
        final FSPath newPath = path.appendRelativePath(relativePath);
        return new FSResource(newPath);
    }

    @Override public HierarchicalResource appendOrReplaceWithPath(ResourcePath other) {
        final FSPath newPath = path.appendOrReplaceWithPath(other);
        return new FSResource(newPath);
    }

    public FSResource appendRelativePath(FSPath relativePath) {
        final FSPath newPath = path.appendRelativePath(relativePath);
        return new FSResource(newPath);
    }


    @Override public FSResource replaceLeaf(String segment) {
        final FSPath newPath = path.replaceLeaf(segment);
        return new FSResource(newPath);
    }

    @Override public FSResource appendToLeaf(String segment) {
        final FSPath newPath = path.appendToLeaf(segment);
        return new FSResource(newPath);
    }

    @Override public FSResource applyToLeaf(Function<String, String> func) {
        final FSPath newPath = path.applyToLeaf(func);
        return new FSResource(newPath);
    }


    @Override public FSResource replaceLeafExtension(String extension) {
        final FSPath newPath = path.replaceLeafExtension(extension);
        return new FSResource(newPath);
    }

    @Override public HierarchicalResource ensureLeafExtension(String extension) {
        final FSPath newPath = path.ensureLeafExtension(extension);
        return new FSResource(newPath);
    }

    @Override public FSResource appendExtensionToLeaf(String extension) {
        final FSPath newPath = path.appendExtensionToLeaf(extension);
        return new FSResource(newPath);
    }

    @Override public FSResource applyToLeafExtension(Function<String, String> func) {
        final FSPath newPath = path.applyToLeafExtension(func);
        return new FSResource(newPath);
    }


    @Override public HierarchicalResourceType getType() throws IOException {
        final BasicFileAttributes attributes = Files.readAttributes(getJavaPath(), BasicFileAttributes.class);
        if(attributes.isRegularFile()) {
            return HierarchicalResourceType.File;
        } else if(attributes.isDirectory()) {
            return HierarchicalResourceType.Directory;
        } else {
            return HierarchicalResourceType.Unknown;
        }
    }

    @Override public boolean isFile() throws IOException {
        final BasicFileAttributes attributes = Files.readAttributes(getJavaPath(), BasicFileAttributes.class);
        return attributes.isRegularFile();
    }

    @Override public boolean isDirectory() throws IOException {
        final BasicFileAttributes attributes = Files.readAttributes(getJavaPath(), BasicFileAttributes.class);
        return attributes.isDirectory();
    }


    @Override public boolean exists() {
        return Files.exists(path.javaPath);
    }

    @Override public boolean isReadable() {
        return Files.isReadable(path.javaPath);
    }

    @Override public boolean isWritable() {
        return Files.isWritable(path.javaPath);
    }

    @Override public Instant getLastModifiedTime() throws IOException {
        return Files.getLastModifiedTime(path.javaPath).toInstant();
    }

    @Override public void setLastModifiedTime(Instant time) throws IOException {
        Files.setLastModifiedTime(path.javaPath, FileTime.from(time));
    }

    @Override public long getSize() throws IOException {
        return Files.size(path.javaPath);
    }


    @Override public Stream<FSResource> list() throws IOException {
        return Files.list(path.javaPath).map(FSResource::new);
    }

    @Override public Stream<FSResource> list(ResourceMatcher matcher) throws IOException {
        try {
            return Files.list(path.javaPath).map(FSResource::new).filter((n) -> {
                try {
                    return matcher.matches(n, this);
                } catch(IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch(UncheckedIOException e) {
            throw e.getCause();
        }
    }


    @Override public Stream<FSResource> walk() throws IOException {
        return Files.walk(path.javaPath).map(FSResource::new);
    }

    @Override public Stream<FSResource> walk(ResourceWalker walker, ResourceMatcher matcher) throws IOException {
        return walk(walker, matcher, null);
    }

    @Override
    public Stream<FSResource> walk(ResourceWalker walker, ResourceMatcher matcher, @Nullable HierarchicalResourceAccess access) throws IOException {
        final Stream.Builder<FSResource> streamBuilder = Stream.builder();
        final ResourceWalkerFileVisitor
            visitor = new ResourceWalkerFileVisitor(walker, matcher, this, streamBuilder, access);
        Files.walkFileTree(path.javaPath, visitor);
        return streamBuilder.build();
    }


    @Override public InputStream newInputStream() throws IOException {
        return Files.newInputStream(path.javaPath, StandardOpenOption.READ);
    }

    @Override public byte[] readBytes() throws IOException {
        return Files.readAllBytes(path.javaPath);
    }

    public List<String> readLines(Charset charset) throws IOException {
        return Files.readAllLines(path.javaPath, charset);
    }

    @Override public String readString(Charset charset) throws IOException {
        return new String(readBytes(), charset);
    }


    @Override public OutputStream newOutputStream() throws IOException {
        return Files.newOutputStream(path.javaPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override public void writeBytes(byte[] bytes) throws IOException {
        Files.write(path.javaPath, bytes);
    }

    public void writeLines(Iterable<String> lines, Charset charset) throws IOException {
        Files.write(path.javaPath, lines, charset);
    }

    @Override public void writeString(String string, Charset charset) throws IOException {
        Files.write(path.javaPath, string.getBytes(charset));
    }


    @Override public void copyTo(HierarchicalResource other) throws IOException {
        if(!(other instanceof FSResource)) {
            throw new ResourceRuntimeException("Cannot copy to '" + other + "', it is not an FSResource");
        }
        copyTo((FSResource) other);
    }

    public void copyTo(FSResource other) throws IOException {
        Files.copy(path.javaPath, other.path.javaPath);
    }

    @Override public void moveTo(HierarchicalResource other) throws IOException {
        if(!(other instanceof FSResource)) {
            throw new ResourceRuntimeException("Cannot move to '" + other + "', it is not an FSResource");
        }
        moveTo((FSResource) other);
    }

    public void moveTo(FSResource other) throws IOException {
        Files.move(path.javaPath, other.path.javaPath);
    }


    @Override public void createFile(boolean createParents) throws IOException {
        if(createParents) {
            createParents();
        }
        Files.createFile(path.javaPath);
    }

    @Override public void createDirectory(boolean createParents) throws IOException {
        if(createParents) {
            Files.createDirectories(path.javaPath);
        }
        if(!exists()) {
            Files.createDirectory(path.javaPath);
        }
    }

    @Override public void createParents() throws IOException {
        final @Nullable FSResource parent = getParent();
        if(parent != null) {
            Files.createDirectories(parent.path.javaPath);
        }
    }


    @Override public void delete(boolean deleteContents) throws IOException {
        if(deleteContents) {
            try {
                if(!Files.exists(path.javaPath)) {
                    return;
                }
                Files.walk(path.javaPath)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch(IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
            } catch(UncheckedIOException e) {
                throw e.getCause();
            }
        } else {
            Files.deleteIfExists(path.javaPath);
        }
    }


    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final FSResource that = (FSResource) o;
        return path.equals(that.path);
    }

    @Override public int hashCode() {
        return path.hashCode();
    }

    @Override public String toString() {
        return path.toString();
    }
}
