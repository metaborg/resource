package mb.resource.fs;

import mb.resource.TreeResourceAccess;
import mb.resource.Resource;
import mb.resource.IORead;
import mb.resource.ResourceRuntimeException;
import mb.resource.TreeResource;
import mb.resource.TreeResourceType;
import mb.resource.IOWrite;
import mb.resource.match.ResourceMatcher;
import mb.resource.path.Path;
import mb.resource.walk.ResourceWalker;
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

public class FSResource implements TreeResource, Resource, IOWrite, IORead, Serializable {
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


    public java.nio.file.Path getJavaPath() {
        return path.javaPath;
    }

    public URI getURI() {
        return path.uri;
    }

    public boolean isLocalPath() {
        return path.javaPath.getFileSystem().equals(FileSystems.getDefault());
    }


    @Override public FSPath getKey() {
        return path;
    }

    @Override public FSPath getPath() {
        return path;
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

    @Override public FSResource appendRelativePath(Path relativePath) {
        final FSPath newPath = path.appendRelativePath(relativePath);
        return new FSResource(newPath);
    }

    public FSResource appendRelativePath(FSPath relativePath) {
        final FSPath newPath = path.appendRelativePath(relativePath);
        return new FSResource(newPath);
    }


    @Override public FSResource replaceLeaf(String str) {
        final FSPath newPath = path.replaceLeaf(str);
        return new FSResource(newPath);
    }

    @Override public FSResource appendToLeaf(String str) {
        final FSPath newPath = path.appendToLeaf(str);
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

    @Override public FSResource appendExtensionToLeaf(String extension) {
        final FSPath newPath = path.appendExtensionToLeaf(extension);
        return new FSResource(newPath);
    }

    @Override public FSResource applyToLeafExtension(Function<String, String> func) {
        final FSPath newPath = path.applyToLeafExtension(func);
        return new FSResource(newPath);
    }


    @Override public TreeResourceType getType() {
        if(!Files.exists(path.javaPath)) {
            return TreeResourceType.NonExistent;
        } else if(Files.isDirectory(path.javaPath)) {
            return TreeResourceType.Directory;
        } else {
            return TreeResourceType.File;
        }
    }

    @Override public boolean isFile() {
        return Files.isRegularFile(path.javaPath);
    }

    @Override public boolean isDirectory() {
        return Files.isDirectory(path.javaPath);
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
    public Stream<FSResource> walk(ResourceWalker walker, ResourceMatcher matcher, @Nullable TreeResourceAccess access) throws IOException {
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


    @Override public void copyTo(TreeResource other) throws IOException {
        if(!(other instanceof FSResource)) {
            throw new ResourceRuntimeException(
                "Cannot copy to '" + other + "', it is not a file system resource");
        }
        copyTo((FSResource) other);
    }

    public void copyTo(FSResource other) throws IOException {
        Files.copy(path.javaPath, other.path.javaPath);
    }

    @Override public void moveTo(TreeResource other) throws IOException {
        if(!(other instanceof FSResource)) {
            throw new ResourceRuntimeException(
                "Cannot copy to '" + other + "', it is not a file system resource");
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
