package mb.resource.classloader;

import mb.resource.ReadableResource;
import mb.resource.ResourceRuntimeException;
import mb.resource.fs.FSResource;
import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.HierarchicalResourceAccess;
import mb.resource.hierarchical.HierarchicalResourceType;
import mb.resource.hierarchical.SegmentsPath;
import mb.resource.hierarchical.SegmentsResource;
import mb.resource.hierarchical.match.ResourceMatcher;
import mb.resource.hierarchical.walk.ResourceWalker;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.stream.Stream;

public class ClassLoaderResource extends SegmentsResource<ClassLoaderResource> implements HierarchicalResource {
    private final ClassLoader classLoader;


    ClassLoaderResource(ClassLoader classLoader, SegmentsPath path) {
        super(path);
        this.classLoader = classLoader;
    }

    ClassLoaderResource(ClassLoader classLoader, String qualifier, String id) {
        super(new SegmentsPath(qualifier, id));
        this.classLoader = classLoader;
    }

    @Override public void close() throws IOException {
        // Nothing to close.
    }


    @Override public HierarchicalResourceType getType() throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support resource types");
    }


    @Override public Stream<ClassLoaderResource> list() throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support listing");
    }

    @Override public Stream<ClassLoaderResource> list(ResourceMatcher matcher) throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support listing");
    }

    @Override public Stream<ClassLoaderResource> walk() throws IOException {
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
        return getUrlToResource() != null;
    }

    @Override public boolean isReadable() throws IOException {
        return true; // Don't know if it is readable; return true although opening an input stream may fail.
    }

    @Override public Instant getLastModifiedTime() throws IOException {
        final URLConnection connection = openConnection();
        final Instant lastModified = Instant.ofEpochMilli(connection.getLastModified());
        closeConnection(connection);
        return lastModified;
    }

    @Override public long getSize() throws IOException {
        final URLConnection connection = openConnection();
        final long size = connection.getContentLengthLong();
        closeConnection(connection);
        return size;
    }

    private void closeConnection(URLConnection connection) throws IOException {
        // URLConnection leaks its input stream when getting metadata: https://bugs.openjdk.java.net/browse/JDK-6956385.
        // HACK: get input stream and immediately close it, which closes the input stream it is leaking.
        connection.getInputStream().close();
        // HACK: for JarURLConnections, also close the JAR file.
        if(connection instanceof JarURLConnection) {
            final JarURLConnection jarUrlConnection = (JarURLConnection)connection;
            jarUrlConnection.getJarFile().close();
        }
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


    /**
     * Gets this resource as a {@link ReadableResource} if it is on the local filesystem, or itself otherwise.
     *
     * The returned resource cannot be used to resolve other resources, as class loaders can be sourced from multiple
     * locations (i.e., the classpath), and the returned resource is only located in one of these locations.
     *
     * @throws ResourceRuntimeException if {@link URL#toURI()} throws.
     */
    public ReadableResource tryAsLocalResource() {
        final @Nullable ReadableResource resource = asLocalResource();
        if(resource != null) return resource;
        return this;
    }

    /**
     * Gets this resource as a {@link ReadableResource} if it is on the local filesystem, or {@code null} if it does not
     * exist nor is on the local filesystem.
     *
     * The returned resource cannot be used to resolve other resources, as class loaders can be sourced from multiple
     * locations (i.e., the classpath), and the returned resource is only located in one of these locations.
     *
     * @throws ResourceRuntimeException if {@link URL#toURI()} throws.
     */
    public @Nullable ReadableResource asLocalResource() {
        final @Nullable URI uri = asLocalUri();
        if(uri == null) return null;
        return new FSResource(uri);
    }

    /**
     * Gets this resource as a {@link File local file} if it is on the local filesystem, or {@code null} if it does not
     * exist nor is on the local filesystem.
     *
     * The returned file should NOT be used to resolve other files, as class loaders can be sourced from multiple
     * locations (i.e., the classpath), and the returned file is only located in one of these locations. Use {@link
     * #getLocations()} to query all locations of this resource.
     *
     * @throws ResourceRuntimeException if {@link URL#toURI()} throws.
     */
    public @Nullable File asLocalFile() {
        final @Nullable URI uri = asLocalUri();
        if(uri == null) return null;
        return new File(uri);
    }

    /**
     * Gets this resource as a {@link URI} if it is on the local filesystem (i.e., the URI has the file: protocol), or
     * {@code null} if it does not exist nor is on the local filesystem.
     *
     * @throws ResourceRuntimeException if {@link URL#toURI()} throws.
     */
    public @Nullable URI asLocalUri() {
        final @Nullable URL url = asLocalUrl();
        if(url == null) return null;
        try {
            return url.toURI();
        } catch(URISyntaxException e) {
            throw new ResourceRuntimeException("Could not get local filesystem URI (with 'file' protocol) for class loader resource '" + path + "'; conversion of URL '" + url + "' to an URI failed", e);
        }
    }

    /**
     * Gets this resource as a {@link URI} if it is on the local filesystem (i.e., the URI has the file: protocol), or
     * {@code null} if it does not exist nor is on the local filesystem.
     *
     * @throws ResourceRuntimeException if {@link URL#toURI()} throws.
     */
    public @Nullable URL asLocalUrl() {
        final @Nullable URL url = getUrlToResource();
        if(url == null) return null;
        if("file".equals(url.getProtocol())) {
            return url;
        }
        return null;
    }

    /**
     * Gets the local filesystem directories and JAR files (located on the local filesystem) this resource is sourced
     * from.
     *
     * @throws IOException              if {@link ClassLoader#getResources(String)} throws.
     * @throws ResourceRuntimeException if no directories nor JAR files were found.
     * @throws ResourceRuntimeException if {@link URL#toURI()} throws.
     */
    public ClassLoaderResourceLocations getLocations() throws IOException {
        final ArrayList<FSResource> directories = new ArrayList<>();
        final ArrayList<JarFileWithPath> jarFiles = new ArrayList<>();
        final Enumeration<URL> resources = classLoader.getResources(path.getId().toString());
        if(!resources.hasMoreElements()) {
            throw new ResourceRuntimeException("Could not get class loader resource locations for '" + path + "'; no locations were found");
        }
        while(resources.hasMoreElements()) {
            final URL url = resources.nextElement();
            final String protocol = url.getProtocol();
            if("file".equals(protocol)) {
                try {
                    final FSResource directory = new FSResource(url.toURI());
                    directories.add(directory);
                } catch(URISyntaxException e) {
                    throw new ResourceRuntimeException("Could not get class loader resource locations for '" + path + "'; conversion of URL '" + url + "' to an URI failed", e);
                }
            } else if("jar".equals(protocol)) {
                final String urlPath = url.getPath();
                final int exclamationMarkIndex = urlPath.indexOf("!");
                final String jarFilePath;
                final String pathInJarFile;
                if(exclamationMarkIndex < 0) {
                    jarFilePath = urlPath;
                    pathInJarFile = "";
                } else {
                    jarFilePath = urlPath.substring(0, exclamationMarkIndex); // skip past '!'
                    pathInJarFile = urlPath.substring(exclamationMarkIndex + 1); // + 1 to skip '!'
                }
                try {
                    final FSResource jarFile = new FSResource(new URI(jarFilePath));
                    jarFiles.add(new JarFileWithPath(jarFile, pathInJarFile));
                } catch(URISyntaxException e) {
                    throw new ResourceRuntimeException("Could not add class loader resource location for '" + url + "'; conversion of nested path '" + jarFilePath + "' to an URI failed", e);
                }
            }
        }
        return new ClassLoaderResourceLocations(directories, jarFiles);
    }


    private @Nullable URL getUrlToResource() {
        return classLoader.getResource(path.getId().toString());
    }

    private URLConnection openConnection() throws IOException {
        final String resourcePath = path.getId().toString();
        final @Nullable URL url = getUrlToResource();
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
