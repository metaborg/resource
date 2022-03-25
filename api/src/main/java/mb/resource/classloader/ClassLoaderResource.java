package mb.resource.classloader;

import mb.resource.ReadableResource;
import mb.resource.ResourceRuntimeException;
import mb.resource.fs.FSResource;
import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.HierarchicalResourceType;
import mb.resource.hierarchical.SegmentsPath;
import mb.resource.hierarchical.SegmentsResource;
import mb.resource.hierarchical.match.ResourceMatcher;
import mb.resource.hierarchical.walk.ResourceWalker;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.Enumeration;
import java.util.stream.Stream;

public class ClassLoaderResource extends SegmentsResource<ClassLoaderResource> implements HierarchicalResource {
    private final ClassLoader classLoader;
    private final ClassLoaderUrlResolver urlResolver;
    private final ClassLoaderToNativeResolver toNativeResolver;


    ClassLoaderResource(ClassLoader classLoader, ClassLoaderUrlResolver urlResolver, ClassLoaderToNativeResolver toNativeResolver, SegmentsPath path) {
        super(path);
        this.classLoader = classLoader;
        this.urlResolver = urlResolver;
        this.toNativeResolver = toNativeResolver;
    }

    ClassLoaderResource(ClassLoader classLoader, ClassLoaderUrlResolver urlResolver, ClassLoaderToNativeResolver toNativeResolver, String id, String qualifier) {
        super(new SegmentsPath(qualifier, id));
        this.classLoader = classLoader;
        this.urlResolver = urlResolver;
        this.toNativeResolver = toNativeResolver;
    }

    @Override public void close() throws IOException {
        // Nothing to close.
    }


    @Override public HierarchicalResourceType getType() throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support resource types");
    }


    @Override public Stream<ClassLoaderResource> list(ResourceMatcher matcher) throws IOException {
        throw new UnsupportedOperationException("Class loader resources do not support listing");
    }

    @Override
    public Stream<ClassLoaderResource> walk(ResourceWalker walker, ResourceMatcher matcher) throws IOException {
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


    @Override public OutputStream openWrite() throws IOException {
        final URLConnection connection = openConnection();
        connection.setDoOutput(true);
        return connection.getOutputStream();
    }

    @Override public OutputStream openWriteAppend() throws IOException {
        return openWrite(); // TODO: not sure if this appends or replaces?
    }

    @Override public OutputStream openWriteExisting() throws IOException {
        if(!exists()) throw new FileNotFoundException("Class loader resource '" + path + "' does not exist");
        return openWrite();
    }

    @Override public OutputStream openWriteNew() throws IOException {
        if(exists()) throw new FileNotFoundException("Class loader resource '" + path + "' already exists");
        return openWrite();
    }


    /**
     * Gets this resource as a {@link ReadableResource} if it is a native resource, or itself otherwise.
     *
     * The returned resource cannot be used to resolve other resources, as class loaders can be sourced from multiple
     * locations (i.e., the classpath), and the returned resource is only located in one of these locations.
     *
     * @throws ResourceRuntimeException if {@link URL#toURI()} throws.
     */
    public ReadableResource tryAsNativeFile() {
        final @Nullable ReadableResource nativeFile = asNativeFile();
        if(nativeFile != null) return nativeFile;
        return this;
    }

    /**
     * Gets this resource as a {@link ReadableResource} if it is a native resource, or {@code null} if it does not exist
     * nor is a native resource.
     *
     * The returned resource cannot be used to resolve other resources, as class loaders can be sourced from multiple
     * locations (i.e., the classpath), and the returned resource is only located in one of these locations.
     *
     * @throws ResourceRuntimeException if {@link URL#toURI()} throws.
     */
    public @Nullable ReadableResource asNativeFile() {
        final @Nullable URL url = getUrlToResource();
        if(url == null) return null;
        final @Nullable ReadableResource nativeFile = toNativeResolver.toNativeFile(url);
        if(nativeFile == null) {
            final @Nullable URL resolvedUrl = urlResolver.resolve(url);
            if(resolvedUrl != null) {
                return toNativeResolver.toNativeFile(resolvedUrl);
            }
        }
        return nativeFile;
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
        final @Nullable URL resolvedUrl = urlResolver.resolve(url);
        if(resolvedUrl != null && "file".equals(resolvedUrl.getProtocol())) {
            return resolvedUrl;
        }
        return null;
    }


    /**
     * Gets the native directories and JAR files (located in a native directory) this resource is sourced from.
     * Unrecognized URLs are returned as well.
     *
     * @throws IOException              if {@link ClassLoader#getResources(String)} throws.
     * @throws ResourceRuntimeException if no directories nor JAR files were found.
     * @throws ResourceRuntimeException if {@link URL#toURI()} throws.
     */
    public ClassLoaderResourceLocations<HierarchicalResource> getLocationsTryAsNative() throws IOException {
        final ClassLoaderResourceLocations<HierarchicalResource> locations = new ClassLoaderResourceLocations<>();
        final Enumeration<URL> resources = classLoader.getResources(path.getId().toString());
        if(!resources.hasMoreElements()) {
            throw new ResourceRuntimeException("Could not get class loader resource locations for '" + path + "'; no locations were found");
        }
        while(resources.hasMoreElements()) {
            final URL url = resources.nextElement();
            processLocationUrlTryAsNative(url, locations);
        }
        return locations;
    }

    // We operate under the expectation that `url` is encoded.
    private void processLocationUrlTryAsNative(URL url, ClassLoaderResourceLocations<HierarchicalResource> locations) throws IOException {
        final @Nullable HierarchicalResource nativeDirectory = toNativeResolver.toNativeDirectory(url);
        if(nativeDirectory != null) {
            locations.directories.add(nativeDirectory);
            return;
        }

        final String protocol = url.getProtocol();
        if("jar".equals(protocol)) {
            final String urlPath = url.getPath();
            final int exclamationMarkIndex = urlPath.indexOf("!");
            final String jarFilePath;
            final String pathInJarFile;
            if(exclamationMarkIndex < 0) {
                jarFilePath = urlPath;
                pathInJarFile = "";
            } else {
                jarFilePath = urlPath.substring(0, exclamationMarkIndex); // before '!'
                pathInJarFile = urlPath.substring(exclamationMarkIndex + 1); // + 1 to skip past '!'
            }
            try {
                final HierarchicalResource jarFile;
                final @Nullable HierarchicalResource nativeJarFile = toNativeResolver.toNativeFile(new URL(jarFilePath));
                if(nativeJarFile != null) {
                    jarFile = nativeJarFile;
                } else {
                    jarFile = new FSResource(new URI(jarFilePath));
                }
                locations.jarFiles.add(new JarFileWithPath<>(jarFile, pathInJarFile));
            } catch(URISyntaxException e) {
                throw new ResourceRuntimeException("Could not add class loader resource location for '" + url + "'; conversion of nested path '" + jarFilePath + "' to an URI failed", e);
            }
        } else {
            final @Nullable URL resolvedUrl = urlResolver.resolve(url);
            if(resolvedUrl != null) {
                processLocationUrlTryAsNative(resolvedUrl, locations);
            } else {
                locations.unrecognizedUrls.add(url);
            }
        }
    }


    /**
     * Gets the local filesystem directories and JAR files (located on the local filesystem) this resource is sourced
     * from. Unrecognized URLs are returned as well.
     *
     * @throws IOException              if {@link ClassLoader#getResources(String)} throws.
     * @throws ResourceRuntimeException if no directories nor JAR files were found.
     * @throws ResourceRuntimeException if {@link URL#toURI()} throws.
     */
    public ClassLoaderResourceLocations<FSResource> getLocations() throws IOException {
        final ClassLoaderResourceLocations<FSResource> locations = new ClassLoaderResourceLocations<>();
        final Enumeration<URL> resources = classLoader.getResources(path.getId().toString());
        if(!resources.hasMoreElements()) {
            throw new ResourceRuntimeException("Could not get class loader resource locations for '" + path + "'; no locations were found");
        }
        while(resources.hasMoreElements()) {
            final URL url = resources.nextElement();
            processLocationUrl(url, locations);
        }
        return locations;
    }

    // We operate under the expectation that `url` is encoded.
    private void processLocationUrl(URL url, ClassLoaderResourceLocations<FSResource> locations) {
        final String protocol = url.getProtocol();
        if("file".equals(protocol)) {
            try {
                final FSResource directory = new FSResource(url.toURI());
                locations.directories.add(directory);
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
                jarFilePath = urlPath.substring(0, exclamationMarkIndex); // before '!'
                pathInJarFile = urlPath.substring(exclamationMarkIndex + 1); // + 1 to skip past '!'
            }
            try {
                final FSResource jarFile = new FSResource(new URI(jarFilePath));
                locations.jarFiles.add(new JarFileWithPath<>(jarFile, pathInJarFile));
            } catch(URISyntaxException e) {
                throw new ResourceRuntimeException("Could not add class loader resource location for '" + url + "'; conversion of nested path '" + jarFilePath + "' to an URI failed", e);
            }
        } else {
            final @Nullable URL resolvedUrl = urlResolver.resolve(url);
            if(resolvedUrl != null) {
                processLocationUrl(resolvedUrl, locations);
            } else {
                locations.unrecognizedUrls.add(url);
            }
        }
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
        return new ClassLoaderResource(classLoader, urlResolver, toNativeResolver, path);
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ClassLoaderResource that = (ClassLoaderResource)o;
        return path.equals(that.path);
    }
}
