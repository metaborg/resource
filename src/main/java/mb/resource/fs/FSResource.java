package mb.resource.fs;

import mb.resource.Resource;
import mb.resource.ResourceRuntimeException;
import mb.resource.fs.match.ResourceMatcher;
import mb.resource.fs.walk.ResourceWalker;
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
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class FSResource implements Resource, Serializable {
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


    public FSPath getPath() {
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


    public @Nullable FSResource getParent() {
        final @Nullable FSPath newPath = path.getParent();
        if(newPath == null) {
            return null;
        }
        return new FSResource(newPath);
    }

    public @Nullable FSResource getRoot() {
        final @Nullable FSPath newPath = path.getRoot();
        if(newPath == null) {
            return null;
        }
        return new FSResource(newPath);
    }

    public @Nullable String getLeaf() {
        return path.getLeaf();
    }

    public @Nullable String getLeafExtension() {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return null;
        }
        return FilenameExtensionUtil.extension(leaf);
    }


    public FSResource appendSegment(String segment) {
        final FSPath newPath = path.appendSegment(segment);
        return new FSResource(newPath);
    }

    public FSResource appendSegments(Iterable<String> segments) {
        final FSPath newPath = path.appendSegments(segments);
        return new FSResource(newPath);
    }

    public FSResource appendSegments(Collection<String> segments) {
        final FSPath newPath = path.appendSegments(segments);
        return new FSResource(newPath);
    }

    public FSResource appendSegments(List<String> segments) {
        final FSPath newPath = path.appendSegments(segments);
        return new FSResource(newPath);
    }

    public FSResource appendSegments(String... segments) {
        final FSPath newPath = path.appendSegments(segments);
        return new FSResource(newPath);
    }

    /**
     * @throws ResourceRuntimeException when relativePath is not a relative path (but instead an absolute one).
     */
    public FSResource appendRelativePath(FSPath relativePath) {
        final FSPath newPath = path.appendRelativePath(relativePath);
        return new FSResource(newPath);
    }


    public FSResource replaceLeaf(String str) {
        final FSPath newPath = path.replaceLeaf(str);
        return new FSResource(newPath);
    }

    public FSResource appendToLeaf(String str) {
        final FSPath newPath = path.appendToLeaf(str);
        return new FSResource(newPath);
    }

    public FSResource applyToLeaf(Function<String, String> func) {
        final FSPath newPath = path.applyToLeaf(func);
        return new FSResource(newPath);
    }

    public FSResource replaceLeafExtension(String extension) {
        final FSPath newPath = path.replaceLeafExtension(extension);
        return new FSResource(newPath);
    }

    public FSResource appendExtensionToLeaf(String extension) {
        final FSPath newPath = path.appendExtensionToLeaf(extension);
        return new FSResource(newPath);
    }

    public FSResource applyToLeafExtension(Function<String, String> func) {
        final FSPath newPath = path.applyToLeafExtension(func);
        return new FSResource(newPath);
    }


    public FSResourceType getType() {
        if(!Files.exists(path.javaPath)) {
            return FSResourceType.NonExistent;
        } else if(Files.isDirectory(path.javaPath)) {
            return FSResourceType.Directory;
        } else {
            return FSResourceType.File;
        }
    }

    public boolean isFile() {
        return Files.isRegularFile(path.javaPath);
    }

    public boolean isDirectory() {
        return Files.isDirectory(path.javaPath);
    }

    public boolean exists() {
        return Files.exists(path.javaPath);
    }

    public boolean isReadable() {
        return Files.isReadable(path.javaPath);
    }

    public boolean isWritable() {
        return Files.isWritable(path.javaPath);
    }

    public Instant getLastModifiedTime() throws IOException {
        return Files.getLastModifiedTime(path.javaPath).toInstant();
    }

    public void setLastModifiedTime(Instant time) throws IOException {
        Files.setLastModifiedTime(path.javaPath, FileTime.from(time));
    }

    public long getSize() throws IOException {
        return Files.size(path.javaPath);
    }


    public Stream<FSResource> list() throws IOException {
        return Files.list(path.javaPath).map(FSResource::new);
    }

    public Stream<FSResource> list(ResourceMatcher matcher) throws IOException {
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


    public Stream<FSResource> walk() throws IOException {
        return Files.walk(path.javaPath).map(FSResource::new);
    }

    public Stream<FSResource> walk(ResourceWalker walker, ResourceMatcher matcher) throws IOException {
        return walk(walker, matcher, null);
    }

    public Stream<FSResource> walk(ResourceWalker walker, ResourceMatcher matcher, @Nullable FSResourceAccess access) throws IOException {
        final Stream.Builder<FSResource> streamBuilder = Stream.builder();
        final ResourceWalkerFileVisitor
            visitor = new ResourceWalkerFileVisitor(walker, matcher, this, streamBuilder, access);
        Files.walkFileTree(path.javaPath, visitor);
        return streamBuilder.build();
    }


    public InputStream newInputStream() throws IOException {
        return Files.newInputStream(path.javaPath, StandardOpenOption.READ);
    }

    public byte[] readBytes() throws IOException {
        return Files.readAllBytes(path.javaPath);
    }

    public List<String> readLines(Charset charset) throws IOException {
        return Files.readAllLines(path.javaPath, charset);
    }

    public String readString(Charset charset) throws IOException {
        return new String(readBytes(), charset);
    }


    public OutputStream newOutputStream() throws IOException {
        return Files.newOutputStream(path.javaPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void writeBytes(byte[] bytes) throws IOException {
        Files.write(path.javaPath, bytes);
    }

    public void writeLines(Iterable<String> lines, Charset charset) throws IOException {
        Files.write(path.javaPath, lines, charset);
    }

    public void writeString(String string, Charset charset) throws IOException {
        Files.write(path.javaPath, string.getBytes(charset));
    }


    public void copyTo(FSResource other) throws IOException {
        Files.copy(path.javaPath, other.path.javaPath);
    }

    public void moveTo(FSResource other) throws IOException {
        Files.move(path.javaPath, other.path.javaPath);
    }


    public void createFile(boolean createParents) throws IOException {
        if(createParents) {
            createParents();
        }
        Files.createFile(path.javaPath);
    }

    public void createFile() throws IOException {
        createFile(false);
    }

    public void createDirectory(boolean createParents) throws IOException {
        if(createParents) {
            Files.createDirectories(path.javaPath);
        }
        if(!exists()) {
            Files.createDirectory(path.javaPath);
        }
    }

    public void createDirectory() throws IOException {
        createDirectory(false);
    }

    public void createParents() throws IOException {
        final @Nullable FSResource parent = getParent();
        if(parent != null) {
            Files.createDirectories(parent.path.javaPath);
        }
    }


    public void delete(boolean deleteContents) throws IOException {
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

    public void delete() throws IOException {
        delete(false);
    }


    @Override public FSPath getKey() {
        return path;
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
