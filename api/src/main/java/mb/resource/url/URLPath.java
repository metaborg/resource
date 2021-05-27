package mb.resource.url;

import mb.resource.ResourceRuntimeException;
import mb.resource.hierarchical.ResourcePath;
import mb.resource.hierarchical.ResourcePathDefaults;
import mb.resource.util.SeparatorUtil;
import mb.resource.util.UriEncode;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class URLPath extends ResourcePathDefaults<URLPath> implements ResourcePath {
    private final URI uri;


    public URLPath(URI uri) {
        this.uri = uri;
    }

    public URLPath(URL url) {
        try {
            this.uri = url.toURI();
        } catch(URISyntaxException e) {
            throw new ResourceRuntimeException("Failed to convert URL '" + url + "' to a URI", e);
        }
    }

    public URLPath(String str) throws URISyntaxException {
        this.uri = UriEncode.encodeToUri(str);
    }


    public URI getURI() {
        return uri;
    }

    public URL getURL() throws MalformedURLException {
        return uri.toURL();
    }


    @Override public String getQualifier() {
        return URLResourceRegistry.qualifier;
    }

    @Override public URI getId() {
        return uri;
    }

    @Override public String getIdAsString() {
        return uri.toString();
    }


    @Override public boolean isAbsolute() {
        return uri.isAbsolute();
    }


    @Override public int getSegmentCount() {
        return 0;
    }

    @Override public Iterable<String> getSegments() {
        return () -> new Iterator<String>() {
            @Override public boolean hasNext() {
                return false;
            }

            @Override public String next() {
                throw new NoSuchElementException();
            }
        };
    }


    @Override public boolean startsWith(ResourcePath prefix) {
        if(!(prefix instanceof URLPath)) {
            throw new ResourceRuntimeException("Cannot check if this path starts with '" + prefix + "', it is not an URLPath");
        }
        return startsWith((URLPath)prefix);
    }

    public boolean startsWith(URLPath prefix) {
        if(!Objects.equals(uri.getScheme(), prefix.uri.getScheme())) return false;
        final @Nullable String path = uri.getPath();
        final @Nullable String prefixPath = prefix.uri.getPath();
        if(path != null && prefixPath != null) {
            return path.startsWith(prefixPath);
        } else if(path == null && prefixPath == null) {
            return true;
        }
        return false;
    }


    @Override public @Nullable URLPath getParent() {
        return null;
    }

    @Override public @Nullable URLPath getRoot() {
        return null;
    }

    @Override public @Nullable String getLeaf() {
        return null;
    }

    @Override public @Nullable String getLeafExtension() {
        return null;
    }


    @Override public URLPath getNormalized() {
        return new URLPath(uri.normalize());
    }

    @Override public String relativize(ResourcePath other) {
        if(!(other instanceof URLPath)) {
            throw new ResourceRuntimeException("Cannot relativize against '" + other + "', it is not an URIPath");
        }
        return relativize((URLPath)other);
    }

    public String relativize(URLPath other) {
        return uri.relativize(other.uri).toString();
    }


    @Override public URLPath appendSegment(String segment) {
        return new URLPath(uri.resolve(segment));
    }

    @Override public URLPath appendSegments(Iterable<String> segments) {
        return new URLPath(uri.resolve(SeparatorUtil.joinWithUnixSeparator(segments)));
    }

    @Override public URLPath appendSegments(Collection<String> segments) {
        return new URLPath(uri.resolve(SeparatorUtil.joinWithUnixSeparator(segments)));
    }

    @Override public URLPath appendSegments(List<String> segments) {
        return new URLPath(uri.resolve(SeparatorUtil.joinWithUnixSeparator(segments)));
    }

    @Override public URLPath appendSegments(String... segments) {
        return new URLPath(uri.resolve(SeparatorUtil.joinWithUnixSeparator(segments)));
    }

    private URI resolve(String other) throws URISyntaxException {
        // Do string append instead of URI#resolve, as it ignores `other` when `uri` is opaque.
        return new URI(uri.toString() + UriEncode.encodeFull(other));
    }


    @Override public URLPath appendRelativePath(String relativePath) {
        try {
            final URI relativeURI = UriEncode.encodeToUri(relativePath);
            if(relativeURI.isAbsolute()) {
                throw new ResourceRuntimeException(
                    "Cannot append '" + relativePath + "', it is an absolute URI");
            }
            return new URLPath(resolve(relativePath));
        } catch(URISyntaxException e) {
            throw new ResourceRuntimeException(
                "Cannot append '" + relativePath + "', it cannot be parsed into an URI", e);
        }
    }

    @Override public URLPath appendOrReplaceWithPath(String other) {
        try {
            final URI otherUri = UriEncode.encodeToUri(other);
            if(otherUri.isAbsolute()) {
                return new URLPath(otherUri);
            }
            return new URLPath(resolve(other));
        } catch(URISyntaxException e) {
            throw new ResourceRuntimeException(
                "Cannot append or replace with '" + other + "', it cannot be parsed into an URI", e);
        }
    }

    @Override public URLPath appendRelativePath(ResourcePath relativePath) {
        if(!(relativePath instanceof URLPath)) {
            throw new ResourceRuntimeException("Cannot append '" + relativePath + "', it is not an URIPath");
        }
        return appendRelativePath((URLPath)relativePath);
    }

    public URLPath appendRelativePath(URLPath relativePath) {
        if(relativePath.isAbsolute()) {
            throw new ResourceRuntimeException(
                "Cannot append '" + relativePath + "', it is an absolute URI");
        }
        // TODO: do string append, as URI#resolve is weird when the URI is opaque?
        return new URLPath(uri.resolve(relativePath.uri));
    }

    @Override public URLPath appendOrReplaceWithPath(ResourcePath other) {
        if(!(other instanceof URLPath)) {
            throw new ResourceRuntimeException("Cannot append or replace with '" + other + "', it is not an URIPath");
        }
        return appendOrReplaceWithPath((URLPath)other);
    }

    public URLPath appendOrReplaceWithPath(URLPath other) {
        // TODO: do string append, as URI#resolve is weird when the URI is opaque?
        return new URLPath(uri.resolve(other.uri));
    }


    @Override public URLPath replaceLeaf(String segment) {
        throw new UnsupportedOperationException();
    }


    @Override protected URLPath self() {
        return this;
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final URLPath that = (URLPath)o;
        return uri.equals(that.uri);
    }

    @Override public int hashCode() {
        return uri.hashCode();
    }

    @Override public String toString() {
        return asString();
    }
}
