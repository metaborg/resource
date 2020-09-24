package mb.resource.classloader;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.HierarchicalResourceAccess;
import mb.resource.hierarchical.HierarchicalResourceType;
import mb.resource.hierarchical.SegmentsPath;
import mb.resource.hierarchical.SegmentsResource;
import mb.resource.hierarchical.match.ResourceMatcher;
import mb.resource.hierarchical.walk.ResourceWalker;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.stream.Stream;

public class ClassLoaderResource extends SegmentsResource<ClassLoaderResource> implements HierarchicalResource {
    private final ClassLoader classLoader;


    ClassLoaderResource(ClassLoader classLoader, SegmentsPath path) {
        super(path);
        this.classLoader = classLoader;
    }

    ClassLoaderResource(ClassLoader classLoader, String qualifier, String path) {
        super(new SegmentsPath(qualifier, path));
        this.classLoader = classLoader;
    }

    @Override public void close() throws IOException {
        // Nothing to close.
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
    public Stream<ClassLoaderResource> walk(ResourceWalker walker, ResourceMatcher matcher, @Nullable HierarchicalResourceAccess access) throws IOException {
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
        return classLoader.getResource(path.getId().toString());
    }

    private URLConnection openConnection() throws IOException {
        final String resourcePath = path.getId().toString();
        final @Nullable URL url = getResource();
        if(url == null) {
            throw new IOException("Resource '" + resourcePath + "' could not be found in class loader '" + classLoader + "'");
        }
        return url.openConnection();
    }


    @Override protected ClassLoaderResource self() {
        return this;
    }

    @Override protected ClassLoaderResource create(SegmentsPath path) {
        return new ClassLoaderResource(classLoader, path);
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ClassLoaderResource that = (ClassLoaderResource)o;
        return path.equals(that.path);
    }
}
