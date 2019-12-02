package mb.resource.url;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.HierarchicalResourceAccess;
import mb.resource.hierarchical.HierarchicalResourceType;
import mb.resource.hierarchical.ResourcePath;
import mb.resource.hierarchical.match.ResourceMatcher;
import mb.resource.hierarchical.walk.ResourceWalker;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class URLResource implements HierarchicalResource {
    private final URLPath path;


    public URLResource(URLPath path) {
        this.path = path;
    }

    public URLResource(URL url) {
        this.path = new URLPath(url);
    }

    @Override public void close() {
        // Nothing to close.
    }


    @Override public URLPath getKey() {
        return path;
    }

    /**
     * Gets the URI of the resource.
     *
     * @return The URI.
     */
    public URI getURI() { return this.path.getURI();}

    /**
     * Gets the URL of the resource.
     *
     * @return The URL.
     */
    public URL getURL() throws MalformedURLException { return this.path.getURL();}

    @Override public boolean exists() {
        return true; // Don't know if it exists; return true although opening a stream may fail.
    }

    @Override public boolean isReadable() {
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


    @Override public boolean isWritable() {
        return true; // Don't know if it is writable; return true although opening an output stream may fail.
    }

    @Override public void setLastModifiedTime(Instant moment) throws IOException {
        // Not supported.
    }

    @Override public OutputStream openWriteExisting() throws IOException {
        final URLConnection connection = openConnection();
        connection.setDoOutput(true);
        return connection.getOutputStream();
    }

    @Override public OutputStream openWriteAppend() throws IOException {
        throw new UnsupportedOperationException("Opening an appending output stream is not supported");
    }


    @Override public @Nullable URLResource getParent() {
        final @Nullable URLPath newPath = path.getParent();
        if(newPath == null) {
            return null;
        }
        return new URLResource(newPath);
    }

    @Override public @Nullable URLResource getRoot() {
        final @Nullable URLPath newPath = path.getRoot();
        if(newPath == null) {
            return null;
        }
        return new URLResource(newPath);
    }

    @Override public @Nullable String getLeaf() {
        return path.getLeaf();
    }

    @Override public @Nullable String getLeafExtension() {
        return path.getLeafExtension();
    }


    @Override public URLResource appendSegment(String segment) {
        return new URLResource(path.appendSegment(segment));
    }

    @Override public URLResource appendSegments(Iterable<String> segments) {
        return new URLResource(path.appendSegments(segments));
    }

    @Override public URLResource appendSegments(Collection<String> segments) {
        return new URLResource(path.appendSegments(segments));
    }

    @Override public URLResource appendSegments(List<String> segments) {
        return new URLResource(path.appendSegments(segments));
    }

    @Override public URLResource appendSegments(String... segments) {
        return new URLResource(path.appendSegments(segments));
    }


    @Override public URLResource appendRelativePath(String relativePath) {
        return new URLResource(path.appendRelativePath(relativePath));
    }

    @Override public URLResource appendOrReplaceWithPath(String other) {
        return new URLResource(path.appendOrReplaceWithPath(other));
    }

    @Override public URLResource appendRelativePath(ResourcePath relativePath) {
        return new URLResource(path.appendRelativePath(relativePath));
    }

    @Override public URLResource appendOrReplaceWithPath(ResourcePath other) {
        return new URLResource(path.appendOrReplaceWithPath(other));
    }


    @Override public URLResource replaceLeaf(String segment) {
        return new URLResource(path.replaceLeaf(segment));
    }

    @Override public URLResource appendToLeaf(String segment) {
        return new URLResource(path.appendToLeaf(segment));
    }

    @Override public URLResource applyToLeaf(Function<String, String> func) {
        return new URLResource(path.applyToLeaf(func));
    }


    @Override public URLResource replaceLeafExtension(String extension) {
        return new URLResource(path.replaceLeafExtension(extension));
    }

    @Override public URLResource ensureLeafExtension(String extension) {
        return new URLResource(path.ensureLeafExtension(extension));
    }

    @Override public URLResource appendExtensionToLeaf(String extension) {
        return new URLResource(path.appendExtensionToLeaf(extension));
    }

    @Override public URLResource applyToLeafExtension(Function<String, String> func) {
        return new URLResource(path.applyToLeafExtension(func));
    }


    @Override public HierarchicalResourceType getType() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override public boolean isFile() throws IOException {
        return false;
    }

    @Override public boolean isDirectory() throws IOException {
        return false;
    }


    @Override public Stream<? extends HierarchicalResource> list() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override public Stream<? extends HierarchicalResource> list(ResourceMatcher matcher) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override public Stream<? extends HierarchicalResource> walk() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<? extends HierarchicalResource> walk(ResourceWalker walker, ResourceMatcher matcher) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<? extends HierarchicalResource> walk(ResourceWalker walker, ResourceMatcher matcher, @Nullable HierarchicalResourceAccess access) throws IOException {
        throw new UnsupportedOperationException();
    }


    @Override public void copyTo(HierarchicalResource other) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override public void copyRecursivelyTo(HierarchicalResource other) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override public void moveTo(HierarchicalResource other) throws IOException {
        throw new UnsupportedOperationException();
    }


    @Override public URLResource createFile(boolean createParents) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override public URLResource createDirectory(boolean createParents) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override public URLResource createParents() throws IOException {
        throw new UnsupportedOperationException();
    }


    @Override public void delete(boolean deleteRecursively) throws IOException {
        throw new UnsupportedOperationException();
    }

    private URLConnection openConnection() throws IOException {
        return getURL().openConnection();
    }


    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final URLResource that = (URLResource)o;
        return path.equals(that.path);
    }

    @Override public int hashCode() {
        return path.hashCode();
    }

    @Override public String toString() {
        return path.toString();
    }
}
