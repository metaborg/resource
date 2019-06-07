package mb.resource.url;

import mb.resource.ReadableResource;
import mb.resource.ResourceKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.time.Instant;

public class URLResource implements ReadableResource, Serializable {
    public final URL url;


    public URLResource(URL url) {
        this.url = url;
    }


    @Override public ResourceKey getKey() {
        return new URLResourceKey(url);
    }

    @Override public boolean exists() {
        return true; // Always exists.
    }

    @Override public boolean isReadable() {
        return true; // Always readable.
    }

    @Override public Instant getLastModifiedTime() {
        return Instant.EPOCH; // Never modified.
    }

    @Override public long getSize() throws IOException {
        return url.openConnection().getContentLengthLong();
    }

    @Override public InputStream newInputStream() throws IOException {
        return url.openStream();
    }


    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final URLResource that = (URLResource) o;
        return url.equals(that.url);
    }

    @Override public int hashCode() {
        return url.hashCode();
    }

    @Override public String toString() {
        return url.toString();
    }
}
