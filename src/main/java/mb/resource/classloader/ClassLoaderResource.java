package mb.resource.classloader;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.HierarchicalResourceAccess;
import mb.resource.hierarchical.HierarchicalResourceDefaults;
import mb.resource.hierarchical.HierarchicalResourceType;
import mb.resource.hierarchical.ResourcePath;
import mb.resource.hierarchical.match.ResourceMatcher;
import mb.resource.hierarchical.walk.ResourceWalker;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.Collection;
import java.util.stream.Stream;

public class ClassLoaderResource extends HierarchicalResourceDefaults<ClassLoaderResource> implements HierarchicalResource {
    private final ClassLoader classLoader;
    private final ClassLoaderResourcePath path;


    ClassLoaderResource(ClassLoader classLoader, ClassLoaderResourcePath path) {
        this.classLoader = classLoader;
        this.path = path;
    }

    ClassLoaderResource(ClassLoader classLoader, String qualifier, String path) {
        this.classLoader = classLoader;
        this.path = new ClassLoaderResourcePath(qualifier, path);
    }


    @Override public void close() throws IOException {
        // Nothing to close.
    }


    @Override public ResourcePath getKey() {
        return path;
    }


    @Override public @Nullable ClassLoaderResource getParent() {
        final @Nullable ClassLoaderResourcePath parent = path.getParent();
        if(parent == null) return null;
        return new ClassLoaderResource(classLoader, parent);
    }

    @Override public @Nullable ClassLoaderResource getRoot() {
        final @Nullable ClassLoaderResourcePath root = path.getRoot();
        if(root == null) return null;
        return new ClassLoaderResource(classLoader, root);
    }


    @Override public ClassLoaderResource appendSegment(String segment) {
        return new ClassLoaderResource(classLoader, path.appendSegment(segment));
    }

    @Override public ClassLoaderResource appendSegments(Iterable<String> segments) {
        return new ClassLoaderResource(classLoader, path.appendSegments(segments));
    }

    @Override public ClassLoaderResource appendSegments(Collection<String> segments) {
        return new ClassLoaderResource(classLoader, path.appendSegments(segments));
    }


    @Override public ClassLoaderResource appendRelativePath(String relativePath) {
        return new ClassLoaderResource(classLoader, path.appendRelativePath(relativePath));
    }

    @Override public ClassLoaderResource appendOrReplaceWithPath(String other) {
        return new ClassLoaderResource(classLoader, path.appendOrReplaceWithPath(other));
    }


    @Override public ClassLoaderResource appendRelativePath(ResourcePath relativePath) {
        return new ClassLoaderResource(classLoader, path.appendRelativePath(relativePath));
    }


    @Override public ClassLoaderResource replaceLeaf(String segment) {
        return new ClassLoaderResource(classLoader, path.replaceLeaf(segment));
    }


    @Override public HierarchicalResourceType getType() throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support resource types");
    }


    @Override public Stream<? extends ClassLoaderResource> list() throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support listing");
    }

    @Override public Stream<? extends ClassLoaderResource> list(ResourceMatcher matcher) throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support listing");
    }

    @Override public Stream<? extends ClassLoaderResource> walk() throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support walking");
    }

    @Override
    public Stream<? extends ClassLoaderResource> walk(ResourceWalker walker, ResourceMatcher matcher, @Nullable HierarchicalResourceAccess access) throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support walking");
    }


    @Override public void copyTo(HierarchicalResource other) throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support copying");
    }

    @Override public void copyRecursivelyTo(HierarchicalResource other) throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support copying");
    }

    @Override public void moveTo(HierarchicalResource other) throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support moving");
    }


    @Override public ClassLoaderResource createFile(boolean createParents) throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support creating files");
    }

    @Override public ClassLoaderResource createDirectory(boolean createParents) throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support creating directories");
    }

    @Override public ClassLoaderResource createParents() throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support creating directories");
    }


    @Override public void delete(boolean deleteRecursively) throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support deletion");
    }


    @Override public boolean exists() throws IOException {
        return getResource() != null;
    }

    @Override public boolean isReadable() throws IOException {
        return true; // Don't know if it is readable; return true although opening an input stream may fail.
    }

    @Override public Instant getLastModifiedTime() throws IOException {
        // TODO: opens new connection every time: not efficient?
        return Instant.ofEpochMilli(openConnection().getLastModified());
    }

    @Override public long getSize() throws IOException {
        // TODO: opens new connection every time: not efficient?
        return openConnection().getContentLengthLong();
    }

    @Override public InputStream openRead() throws IOException {
        return openConnection().getInputStream();
    }


    @Override public boolean isWritable() throws IOException {
        return true; // Don't know if it is writable; return true although opening an output stream may fail.
    }

    @Override public void setLastModifiedTime(Instant moment) throws IOException {
        // Not supported.
    }

    @Override public OutputStream openWriteAppend() throws IOException {
        final URLConnection connection = openConnection();
        connection.setDoOutput(true);
        return connection.getOutputStream();
    }

    @Override public OutputStream openWriteExisting() throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support opening an appending output stream");
    }


    private @Nullable URL getResource() {
        return classLoader.getResource(path.toString());
    }

    private URLConnection openConnection() throws IOException {
        final String resourcePath = path.toString();
        final @Nullable URL url = getResource();
        if(url == null) {
            throw new IOException("Resource '" + resourcePath + "' could not be found in class loader '" + classLoader + "'");
        }
        return url.openConnection();
    }


    @Override protected ClassLoaderResource self() {
        return this;
    }


    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ClassLoaderResource that = (ClassLoaderResource)o;
        return path.equals(that.path);
    }
}
